package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.request.CarModelCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.CarModelUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.CarModelResponse;
import com.Nhom7.DACN_KTPM.service.CarModelService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CarModelController {

    CarModelService carModelService;

    @GetMapping
    ApiResponse<List<CarModelResponse>> getAllModels() {
        log.info("Request to get ALL car models");
        return ApiResponse.<List<CarModelResponse>>builder()
                .result(carModelService.getAllCarModel())
                .build();
    }

    @GetMapping("/active")
    ApiResponse<List<CarModelResponse>> getAllActiveModels() {
        log.info("Request to get all active car models");
        return ApiResponse.<List<CarModelResponse>>builder()
                .result(carModelService.getActiveCarModels()) // Lưu ý: Hàm này trong Service phải có
                .build();
    }

    @PostMapping
    ApiResponse<CarModelResponse> createModel(@RequestBody @Valid CarModelCreationRequest request) {
        log.info("Request to create car model: {}", request.getName());
        return ApiResponse.<CarModelResponse>builder()
                .result(carModelService.createCarModel(request))
                .message("Car model created successfully")
                .build();
    }

    // 👇 SỬA: Integer -> Long
    @GetMapping("/{id}")
    ApiResponse<CarModelResponse> getModelById(@PathVariable Long id) {
        log.info("Request to get car model by ID: {}", id);
        return ApiResponse.<CarModelResponse>builder()
                .result(carModelService.getCarModel(id))
                .build();
    }

    // 👇 SỬA: Integer -> Long
    @PutMapping("/{id}")
    ApiResponse<CarModelResponse> updateModel(@PathVariable Long id, @RequestBody @Valid CarModelUpdateRequest request) {
        log.info("Request to update car model ID: {}", id);
        return ApiResponse.<CarModelResponse>builder()
                .result(carModelService.updateCarModel(id, request))
                .message("Car model updated successfully")
                .build();
    }

    // 👇 SỬA: Integer -> Long
    @DeleteMapping("/{id}")
    ApiResponse<String> deleteModel(@PathVariable Long id) {
        log.info("Request to delete car model ID: {}", id);
        carModelService.deleteCarModel(id);
        return ApiResponse.<String>builder()
                .result("Dòng xe đã được xóa thành công")
                .build();
    }
}