package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.request.BankCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.BankUpdateRequest;

import com.Nhom7.DACN_KTPM.dto.response.BankResponse;
import com.Nhom7.DACN_KTPM.service.BankService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BankController {

    BankService bankService;

    @GetMapping("/all")
        // @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<BankResponse>> getAllBanks() {
        log.info("Request to get ALL banks");
        return ApiResponse.<List<BankResponse>>builder()
                .result(bankService.getAllBankss())
                .build();
    }

    @PostMapping
        // @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<BankResponse> createBank(@RequestBody @Valid BankCreationRequest request) {
        log.info("Request to create bank: {}", request.getName());
        return ApiResponse.<BankResponse>builder()
                .result(bankService.createBank(request))
                .message("Bank created successfully")
                .build();
    }

    @GetMapping("/active") // Endpoint for customer view (estimation page)
    ApiResponse<List<BankResponse>> getAllActiveBanks() {
        log.info("Request to get all active banks");
        return ApiResponse.<List<BankResponse>>builder()
                .result(bankService.getAllActiveBanks())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<BankResponse> getBankById(@PathVariable Integer id) {
        log.info("Request to get bank by ID: {}", id);
        return ApiResponse.<BankResponse>builder()
                .result(bankService.getBank(id))
                .build();
    }

    @PutMapping("/{id}")
        // @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<BankResponse> updateBank(@PathVariable Integer id, @RequestBody @Valid BankUpdateRequest request) {
        log.info("Request to update bank ID: {}", id);
        return ApiResponse.<BankResponse>builder()
                .result(bankService.updateBank(id, request))
                .message("Bank updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
        // @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<String> deleteBank(@PathVariable Integer id) {
        log.info("Request to delete bank ID: {}", id);
        bankService.deleteBank(id);
        return ApiResponse.<String>builder()
                .result("Ngân hàng đã được xóa thành công")
                .build();
    }

}