package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.response.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.request.CreateConsultationRequest;
import com.Nhom7.DACN_KTPM.dto.response.ConsultationResponse;
import com.Nhom7.DACN_KTPM.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import com.Nhom7.DACN_KTPM.dto.request.UpdateConsultationStatusRequest;
import java.util.List;

@RestController
@RequestMapping("/api") // ⚠️ QUAN TRỌNG: Đưa về path gốc là /api
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsultationController {

    ConsultationService consultationService;

    // 1. API Khách hàng gửi yêu cầu (Public)
    // URL: POST /api/public/consultations
    @PostMapping("/public/consultations")
    public ApiResponse<ConsultationResponse> submitConsultation(
            @Valid @RequestBody CreateConsultationRequest request) {
        return ApiResponse.<ConsultationResponse>builder()
                .code(1000)
                .message("Gửi yêu cầu thành công")
                .result(consultationService.createConsultation(request))
                .build();
    }

    // 2. API Nhân viên lấy danh sách (Private - Cần Token)
    // URL: GET /api/consultations
    @GetMapping("/consultations") // ⚠️ Bỏ chữ 'public' đi
    public ApiResponse<List<ConsultationResponse>> getAllConsultations(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String province
    ) {
        return ApiResponse.<List<ConsultationResponse>>builder()
                .code(1000)
                .result(consultationService.getAllConsultations(status, province))
                .build();
    }

    // 3. API Nhân viên cập nhật trạng thái/nhận khách (Private - Cần Token)
    // URL: PUT /api/consultations/{id}
    @PutMapping("/consultations/{id}") // ⚠️ Bỏ chữ 'public' đi
    public ApiResponse<ConsultationResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateConsultationStatusRequest request) {

        return ApiResponse.<ConsultationResponse>builder()
                .code(1000)
                .result(consultationService.updateConsultationStatus(id, request))
                .build();
    }

    // 4. API Lấy danh sách khách hàng đặt cọc (Private - Cần Token)
    // URL: GET /api/customers-with-staff
    @GetMapping("/customers-with-staff")
    public ApiResponse<List<ConsultationResponse>> getCustomersWithStaff() {
        return ApiResponse.<List<ConsultationResponse>>builder()
                .code(1000)
                .result(consultationService.getAllConsultations(null, null))
                .build();
    }
}