package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.request.CalculationRequestDTO;

import com.Nhom7.DACN_KTPM.dto.request.InstallmentPlanDTO;
import com.Nhom7.DACN_KTPM.dto.response.LoanPolicyResponse;
import com.Nhom7.DACN_KTPM.service.FinanceService;
import com.Nhom7.DACN_KTPM.service.LoanPolicyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FinanceController {

    LoanPolicyService loanPolicyService;
    FinanceService financeService;

    @GetMapping("/policies")
    public ApiResponse<List<LoanPolicyResponse>> getActiveLoanPolicies(
            @RequestParam(required = false) Integer bankId
    ) {
        log.info("Request received for active loan policies, bankId: {}", bankId);
        return ApiResponse.<List<LoanPolicyResponse>>builder()
                .result(loanPolicyService.getActiveLoanPolicies(bankId))
                .build();
    }

    @PostMapping("/calculate")
    public ApiResponse<InstallmentPlanDTO> calculateLoan(
            @Valid @RequestBody CalculationRequestDTO request
    ) {
        log.info("Calculation request received: {}", request);
        InstallmentPlanDTO plan = financeService.calculateLoanInstallment(request);
        return ApiResponse.<InstallmentPlanDTO>builder()
                .result(plan)
                .build();
    }
}
