package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.request.ContractCreationRequest;
import com.Nhom7.DACN_KTPM.entity.Contract;
import com.Nhom7.DACN_KTPM.entity.Deposit;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.repository.ContractRepository;
import com.Nhom7.DACN_KTPM.repository.DepositRepository;
import com.Nhom7.DACN_KTPM.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Import thêm các gói này
import org.springframework.security.core.context.SecurityContextHolder;
import com.Nhom7.DACN_KTPM.entity.User;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final DepositRepository depositRepository;
    private final UserRepository userRepository; // <--- Inject thêm UserRepository

    public Contract createContract(ContractCreationRequest request) {
        // 1. Kiểm tra đơn cọc (Giữ nguyên code cũ)
        Deposit deposit = depositRepository.findById(request.getDepositId())
                .orElseThrow(() -> new RuntimeException("Đơn đặt cọc không tồn tại"));

        if (!"APPROVED".equals(deposit.getStatus())) {
            throw new RuntimeException("Chỉ được tạo hợp đồng cho đơn cọc ĐÃ DUYỆT");
        }

        if (contractRepository.existsByDepositId(request.getDepositId())) {
            throw new RuntimeException("Đơn cọc này đã được tạo hợp đồng rồi!");
        }

        // 2. LẤY NGƯỜI DÙNG ĐANG ĐĂNG NHẬP (SỬA ĐOẠN NÀY)
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người tạo (Admin)"));

        // 3. Tạo hợp đồng
        Contract contract = Contract.builder()
                .contractNumber(request.getContractNumber())
                .deposit(deposit)
                .finalPrice(request.getFinalPrice())
                .status("DRAFT")
                .createdBy(currentUser.getId()) // <--- LƯU ID THẬT CỦA USER, KHÔNG DÙNG "ADMIN"
                .build();

        return contractRepository.save(contract);
    }
}