package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.request.CreateConsultationRequest;
import com.Nhom7.DACN_KTPM.dto.response.ConsultationResponse;
import com.Nhom7.DACN_KTPM.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsultationController {

    ConsultationService consultationService;

    @PostMapping
    public ApiResponse<ConsultationResponse> submitConsultation(
            @Valid @RequestBody CreateConsultationRequest request) {

        return ApiResponse.<ConsultationResponse>builder()
                .result(consultationService.createConsultation(request))
                .build();
    }
}