package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.request.CarVariantCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.CarVariantUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.CarVariantBasicResponse;
import com.Nhom7.DACN_KTPM.dto.response.CarVariantDetailResponse;
import com.Nhom7.DACN_KTPM.service.CarVariantService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CarVariantController {

    CarVariantService carVariantService;

    @GetMapping
        // 👇 SỬA: Integer -> Long
    ApiResponse<List<CarVariantBasicResponse>> getAllVariants(
            @RequestParam(required = false) Long modelId
    ) {
        if (modelId != null) {
            log.info("Request to get active car variants for model ID: {}", modelId);
            return ApiResponse.<List<CarVariantBasicResponse>>builder()
                    .result(carVariantService.getActiveVariantsByModel(modelId))
                    .build();
        }
        log.info("Request to get ALL car variants (No filter)");
        return ApiResponse.<List<CarVariantBasicResponse>>builder()
                .result(carVariantService.getAllVariant())
                .build();
    }

    @PostMapping
    ApiResponse<CarVariantDetailResponse> createVariant(@RequestBody @Valid CarVariantCreationRequest request) {
        log.info("Request to create car variant: {} for model ID: {}", request.getName(), request.getModelId());
        return ApiResponse.<CarVariantDetailResponse>builder()
                .result(carVariantService.createVariant(request))
                .message("Car variant created successfully")
                .build();
    }

    @GetMapping("/{id}/details")
        // 👇 SỬA: Integer -> Long
    ApiResponse<CarVariantDetailResponse> getVariantDetail(@PathVariable Long id) {
        log.info("Request to get car variant details for ID: {}", id);
        return ApiResponse.<CarVariantDetailResponse>builder()
                .result(carVariantService.getVariantDetail(id))
                .build();
    }

    @PutMapping("/{id}")
        // 👇 SỬA: Integer -> Long
    ApiResponse<CarVariantDetailResponse> updateVariant(@PathVariable Long id, @RequestBody @Valid CarVariantUpdateRequest request) {
        log.info("Request to update car variant ID: {}", id);
        return ApiResponse.<CarVariantDetailResponse>builder()
                .result(carVariantService.updateVariant(id, request))
                .message("Car variant updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
        // 👇 SỬA: Integer -> Long
    ApiResponse<String> deleteVariant(@PathVariable Long id) {
        log.info("Request to delete car variant ID: {}", id);
        carVariantService.deleteVariant(id);
        return ApiResponse.<String>builder()
                .result("Phiên bản xe đã được xóa thành công")
                .build();
    }
}