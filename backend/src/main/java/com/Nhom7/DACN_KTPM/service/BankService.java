package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.request.BankCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.BankUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.BankResponse;
import com.Nhom7.DACN_KTPM.entity.Bank;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.mapper.BankMapper;
import com.Nhom7.DACN_KTPM.repository.BankRepository;
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
public class BankService {

    BankRepository bankRepository;
    BankMapper bankMapper;


    @Transactional(readOnly = true)
    // @PreAuthorize("hasRole('ADMIN')")
    public List<BankResponse> getAllBankss() {
        log.info("Fetching ALL banks (including inactive)");
        // Use the default findAll() method from JpaRepository
        List<Bank> banks = bankRepository.findAll();
        return bankMapper.toBankResponseList(banks);
    }

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public BankResponse createBank(BankCreationRequest request) {
        log.info("Creating new bank: {}", request.getName());
        if (bankRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.BANK_EXISTED);
        }

        Bank bank = bankMapper.toBank(request);

        try {
            bank = bankRepository.save(bank);
        } catch (DataIntegrityViolationException e) {
            log.error("Error creating bank - name likely exists: {}", request.getName(), e);
            throw new AppException(ErrorCode.BANK_EXISTED);
        }

        return bankMapper.toBankResponse(bank);
    }

    @Transactional(readOnly = true)
    public List<BankResponse> getAllActiveBanks() {
        log.info("Fetching all active banks");
        return bankMapper.toBankResponseList(
                bankRepository.findByIsActiveTrueOrderByNameAsc());
    }

    @Transactional(readOnly = true)
    public BankResponse getBank(Integer id) {
        log.info("Fetching bank with ID: {}", id);
        return bankMapper.toBankResponse(
                bankRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.BANK_NOT_FOUND)));
    }

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public BankResponse updateBank(Integer id, BankUpdateRequest request) {
        log.info("Updating bank with ID: {}", id);
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BANK_NOT_FOUND));

        if (!bank.getName().equalsIgnoreCase(request.getName()) &&
                bankRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.BANK_EXISTED);
        }

        bankMapper.updateBank(bank, request);

        try {
            bank = bankRepository.save(bank);
        } catch (DataIntegrityViolationException e) {
            log.error("Error updating bank ID {} - name likely exists: {}", id, request.getName(), e);
            throw new AppException(ErrorCode.BANK_EXISTED);
        }

        return bankMapper.toBankResponse(bank);
    }

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public void deleteBank(Integer id) {
        log.info("Deleting bank with ID: {}", id);
        if (!bankRepository.existsById(id)) {
            throw new AppException(ErrorCode.BANK_NOT_FOUND);
        }
        // Consider checking if the bank is used by any LoanPolicy before deleting
        try {
            bankRepository.deleteById(id);
            log.info("Deleted bank with ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Cannot delete bank ID {} as it is currently in use by loan policies.", id, e);
            throw new AppException(ErrorCode.BANK_IN_USE); // Need ErrorCode.BANK_IN_USE
        }
    }
}
