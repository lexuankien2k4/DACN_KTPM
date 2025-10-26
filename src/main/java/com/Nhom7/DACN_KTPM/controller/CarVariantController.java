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
@RequestMapping("/variants") // Đường dẫn cho variants
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CarVariantController {

    CarVariantService carVariantService;

    @GetMapping
        // @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<CarVariantBasicResponse>> getAllVariants() {
        log.info("Request to get ALL car variants");
        return ApiResponse.<List<CarVariantBasicResponse>>builder()
                .result(carVariantService.getAllVariant())
                .build();
    }

    @PostMapping
        // @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<CarVariantDetailResponse> createVariant(@RequestBody @Valid CarVariantCreationRequest request) {
        log.info("Request to create car variant: {} for model ID: {}", request.getName(), request.getModelId());
        return ApiResponse.<CarVariantDetailResponse>builder()
                .result(carVariantService.createVariant(request))
                .message("Car variant created successfully")
                .build();
    }

    @GetMapping // Get active variants by modelId (for customer view)
    ApiResponse<List<CarVariantBasicResponse>> getActiveVariantsByModel(@RequestParam Long modelId) {
        log.info("Request to get active variants for model ID: {}", modelId);
        return ApiResponse.<List<CarVariantBasicResponse>>builder()
                .result(carVariantService.getActiveVariantsByModel(modelId))
                .build();
    }

    @GetMapping("/{id}/details") // Get full details of one variant
    ApiResponse<CarVariantDetailResponse> getVariantDetail(@PathVariable Long id) {
        log.info("Request to get car variant details for ID: {}", id);
        return ApiResponse.<CarVariantDetailResponse>builder()
                .result(carVariantService.getVariantDetail(id))
                .build();
    }

    @PutMapping("/{id}")
        // @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<CarVariantDetailResponse> updateVariant(@PathVariable Long id, @RequestBody @Valid CarVariantUpdateRequest request) {
        log.info("Request to update car variant ID: {}", id);
        return ApiResponse.<CarVariantDetailResponse>builder()
                .result(carVariantService.updateVariant(id, request))
                .message("Car variant updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
        // @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<String> deleteVariant(@PathVariable Long id) {
        log.info("Request to delete car variant ID: {}", id);
        carVariantService.deleteVariant(id);
        return ApiResponse.<String>builder()
                .result("Phiên bản xe đã được xóa thành công")
                .build();
    }

}
