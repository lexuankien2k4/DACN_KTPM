package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.request.LoanPolicyCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.LoanPolicyUpdateRequest;

import com.Nhom7.DACN_KTPM.dto.response.LoanPolicyResponse;
import com.Nhom7.DACN_KTPM.service.LoanPolicyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/policies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LoanPolicyController {

    LoanPolicyService loanPolicyService;

    // CREATE
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<LoanPolicyResponse> createPolicy(@Valid @RequestBody LoanPolicyCreationRequest request) {
        log.info("Request received to create loan policy: {}", request.getName());
        return ApiResponse.<LoanPolicyResponse>builder()
                .result(loanPolicyService.createPolicy(request))
                .message("Loan policy created successfully")
                .build();
    }

    // READ Active (For Customer View)
    @GetMapping("/active")
    public ApiResponse<List<LoanPolicyResponse>> getActiveLoanPolicies(
            @RequestParam(required = false) Integer bankId
    ) {
        log.info("Request received for active loan policies, bankId: {}", bankId);
        return ApiResponse.<List<LoanPolicyResponse>>builder()
                .result(loanPolicyService.getActiveLoanPolicies(bankId))
                .build();
    }


    @GetMapping("/all")
    // @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<LoanPolicyResponse>> getAllPolicies() {
        log.info("Request received for ALL loan policies");
        return ApiResponse.<List<LoanPolicyResponse>>builder()
                .result(loanPolicyService.getAllLoanPolicies())
                .build();
    }


    @GetMapping("/{id}")
    public ApiResponse<LoanPolicyResponse> getPolicyById(@PathVariable Integer id) {
        log.info("Request received for loan policy ID: {}", id);
        return ApiResponse.<LoanPolicyResponse>builder()
                .result(loanPolicyService.getPolicy(id))
                .build();
    }


    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<LoanPolicyResponse> updatePolicy(@PathVariable Integer id, @Valid @RequestBody LoanPolicyUpdateRequest request) {
        log.info("Request received to update loan policy ID: {}", id);
        return ApiResponse.<LoanPolicyResponse>builder()
                .result(loanPolicyService.updatePolicy(id, request))
                .message("Loan policy updated successfully")
                .build();
    }


    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deletePolicy(@PathVariable Integer id) {
        log.info("Request received to delete loan policy ID: {}", id);
        loanPolicyService.deletePolicy(id);
        return ApiResponse.<String>builder()
                .result("Chính sách cho vay đã được xóa thành công")
                .build();
    }

    @GetMapping("/by-bank/{bankId}/active")
    public ApiResponse<List<LoanPolicyResponse>> getActivePoliciesByBankId(@PathVariable Integer bankId) {
        log.info("Request received for active loan policies for Bank ID: {}", bankId);
        // Sử dụng lại phương thức hiện có trong Service
        return ApiResponse.<List<LoanPolicyResponse>>builder()
                .result(loanPolicyService.getActiveLoanPolicies(bankId))
                .build();
    }
}