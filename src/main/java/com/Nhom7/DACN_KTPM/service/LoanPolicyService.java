package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.request.LoanPolicyCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.LoanPolicyUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.LoanPolicyResponse;
import com.Nhom7.DACN_KTPM.entity.Bank;
import com.Nhom7.DACN_KTPM.entity.LoanPolicy;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.mapper.LoanPolicyMapper;
import com.Nhom7.DACN_KTPM.repository.BankRepository;
import com.Nhom7.DACN_KTPM.repository.LoanPolicyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LoanPolicyService {

    LoanPolicyRepository loanPolicyRepository;
    BankRepository bankRepository; // Cần để lấy đối tượng Bank
    LoanPolicyMapper loanPolicyMapper;

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public LoanPolicyResponse createPolicy(LoanPolicyCreationRequest request) {
        log.info("Creating new loan policy: {} for bank ID: {}", request.getName(), request.getBankId());

        // Kiểm tra tên chính sách đã tồn tại cho ngân hàng này chưa
        if (loanPolicyRepository.existsByNameAndBankId(request.getName(), request.getBankId())) {
            throw new AppException(ErrorCode.LOAN_POLICY_EXISTED); // Cần định nghĩa mã lỗi
        }

        // Tìm Bank tương ứng
        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() -> new AppException(ErrorCode.BANK_NOT_FOUND));

        LoanPolicy policy = loanPolicyMapper.toLoanPolicy(request);
        policy.setBank(bank); // Gán quan hệ

        try {
            policy = loanPolicyRepository.save(policy);
        } catch (DataIntegrityViolationException e) {
            log.error("Error creating loan policy - likely constraint violation: {}", request.getName(), e);
            throw new AppException(ErrorCode.LOAN_POLICY_EXISTED); // Hoặc lỗi chung
        }

        return loanPolicyMapper.toLoanPolicyResponse(policy);
    }

    @Transactional(readOnly = true)
    public List<LoanPolicyResponse> getActiveLoanPolicies(Integer bankId) {
        List<LoanPolicy> policies;
        if (bankId != null) {
            log.info("Fetching active loan policies for bank ID: {}", bankId);
            policies = loanPolicyRepository.findByBankIdAndIsActiveTrue(bankId);
        } else {
            log.info("Fetching all active loan policies");
            policies = loanPolicyRepository.findByIsActiveTrue();
        }
        return loanPolicyMapper.toLoanPolicyResponseList(policies);
    }

    // Lấy tất cả (bao gồm inactive - cho admin)
    @Transactional(readOnly = true)
    // @PreAuthorize("hasRole('ADMIN')")
    public List<LoanPolicyResponse> getAllLoanPolicies() {
        log.info("Fetching ALL loan policies");
        return loanPolicyMapper.toLoanPolicyResponseList(loanPolicyRepository.findAll());
    }

    @Transactional(readOnly = true)
    public LoanPolicyResponse getPolicy(Integer id) {
        log.info("Fetching loan policy with ID: {}", id);
        return loanPolicyMapper.toLoanPolicyResponse(
                loanPolicyRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.LOAN_POLICY_NOT_FOUND))); // Cần định nghĩa mã lỗi
    }

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public LoanPolicyResponse updatePolicy(Integer id, LoanPolicyUpdateRequest request) {
        log.info("Updating loan policy with ID: {}", id);
        LoanPolicy policy = loanPolicyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LOAN_POLICY_NOT_FOUND));

        // Kiểm tra tên mới có bị trùng với policy khác của cùng ngân hàng không
        if (!policy.getName().equalsIgnoreCase(request.getName()) &&
                loanPolicyRepository.existsByNameAndBankIdAndIdNot(request.getName(), policy.getBank().getId(), id)) {
            throw new AppException(ErrorCode.LOAN_POLICY_EXISTED);
        }

        loanPolicyMapper.updateLoanPolicy(policy, request);

        try {
            policy = loanPolicyRepository.save(policy);
        } catch (DataIntegrityViolationException e) {
            log.error("Error updating policy ID {} - name likely exists: {}", id, request.getName(), e);
            throw new AppException(ErrorCode.LOAN_POLICY_EXISTED);
        }

        return loanPolicyMapper.toLoanPolicyResponse(policy);
    }

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public void deletePolicy(Integer id) {
        log.info("Deleting loan policy with ID: {}", id);
        if (!loanPolicyRepository.existsById(id)) {
            throw new AppException(ErrorCode.LOAN_POLICY_NOT_FOUND);
        }
        // Có thể thêm kiểm tra xem Policy có đang được tham chiếu ở đâu không trước khi xóa
        loanPolicyRepository.deleteById(id);
        log.info("Deleted loan policy with ID: {}", id);
    }
}
