package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.request.UpdateConsultationStatusRequest;
import com.Nhom7.DACN_KTPM.dto.response.ConsultationResponse;
import com.Nhom7.DACN_KTPM.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/consultations") // Endpoint được bảo vệ cho admin
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminConsultationController {

    ConsultationService consultationService;

    @GetMapping
    public ApiResponse<List<ConsultationResponse>> getAllConsultations(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String province) {

        return ApiResponse.<List<ConsultationResponse>>builder()
                .result(consultationService.getAllConsultations(status, province))
                .build();
    }

    @PutMapping("/{id}/status")
    public ApiResponse<ConsultationResponse> updateStatus(
            @PathVariable("id") Integer requestId,
            @Valid @RequestBody UpdateConsultationStatusRequest request) {

        return ApiResponse.<ConsultationResponse>builder()
                .result(consultationService.updateConsultationStatus(requestId, request))
                .build();
    }

}