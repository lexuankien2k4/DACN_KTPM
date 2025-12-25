package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.PaymentCreationRequest;
import com.Nhom7.DACN_KTPM.dto.response.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.response.DepositResponse;
import com.Nhom7.DACN_KTPM.dto.response.PaymentResponse;
import com.Nhom7.DACN_KTPM.dto.response.VnPayResponse;
import com.Nhom7.DACN_KTPM.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment")
    public ApiResponse<PaymentResponse> createPayment(
            @RequestBody PaymentCreationRequest request,
            HttpServletRequest httpServletRequest
    ) {
        PaymentResponse result = paymentService.createPayment(request, httpServletRequest);

        return ApiResponse.<PaymentResponse>builder()
                .code(1000) // Giả sử 1000 là success code
                .result(result)
                .build();
    }
    @GetMapping("/vnpay-callback")
    public ApiResponse<VnPayResponse> handleVnPayCallback(HttpServletRequest request) {
        VnPayResponse status = paymentService.handleVnPayCallback(request); // Dùng VnPayResponse thay vì PaymentResponse.VnPayResponse
        return ApiResponse.<VnPayResponse>builder()
                .code(1000)
                .result(status)
                .build();
    }
    // GET: Lấy danh sách
    @GetMapping
    public ApiResponse<List<DepositResponse>> getAllDeposits() {
        return ApiResponse.<List<DepositResponse>>builder()
                .code(1000)
                .result(paymentService.getAllDeposits())
                .build();
    }

    // PUT: Cập nhật trạng thái (Dùng cho nút Duyệt/Hủy)
    @PutMapping("/{id}/status")
    public ApiResponse<String> updateStatus(@PathVariable Long id, @RequestBody String status) {
        paymentService.updateDepositStatus(id, status);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Cập nhật thành công")
                .build();
    }
}