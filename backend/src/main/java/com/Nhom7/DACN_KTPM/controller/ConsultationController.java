package com.Nhom7.DACN_KTPM.controller;

// ĐÃ SỬA: Import đúng package response
import com.Nhom7.DACN_KTPM.dto.response.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.request.CreateConsultationRequest;
import com.Nhom7.DACN_KTPM.dto.response.ConsultationResponse;
import com.Nhom7.DACN_KTPM.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/consultations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsultationController {

    ConsultationService consultationService;

    @PostMapping
    public ApiResponse<ConsultationResponse> submitConsultation(
            @Valid @RequestBody CreateConsultationRequest request) {

        return ApiResponse.<ConsultationResponse>builder()
                .code(1000)
                .message("Gửi yêu cầu thành công")
                .result(consultationService.createConsultation(request))
                .build();
    }
    // 👇 API 2: Lấy danh sách (BỔ SUNG THÊM CÁI NÀY) 👇
    @GetMapping
    public ApiResponse<List<ConsultationResponse>> getAllConsultations(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String province
    ) {
        return ApiResponse.<List<ConsultationResponse>>builder()
                .code(1000)
                .result(consultationService.getAllConsultations(status, province))
                .build();
    }
}