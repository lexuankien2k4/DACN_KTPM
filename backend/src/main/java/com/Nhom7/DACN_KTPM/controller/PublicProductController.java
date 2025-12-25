package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.response.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.response.ProductDisplayResponse;
import com.Nhom7.DACN_KTPM.service.PublicProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PublicProductController {

    private final PublicProductService productService;

    // 1. Lấy xe điện (Giữ nguyên hoặc đổi tên endpoint tùy ý)
    @GetMapping("/ev")
    public ApiResponse<List<ProductDisplayResponse>> getElectricCars() {
        return ApiResponse.<List<ProductDisplayResponse>>builder()
                .result(productService.getProductsByCategory("ev"))
                .build();
    }

    // 2. Lấy xe xăng (ID 2)
    @GetMapping("/gasoline")
    public ApiResponse<List<ProductDisplayResponse>> getGasolineCars() {
        return ApiResponse.<List<ProductDisplayResponse>>builder()
                .result(productService.getProductsByCategory("gasoline"))
                .build();
    }

    // 3. Lấy xe dịch vụ (ID 3)
    @GetMapping("/service")
    public ApiResponse<List<ProductDisplayResponse>> getServiceCars() {
        return ApiResponse.<List<ProductDisplayResponse>>builder()
                .result(productService.getProductsByCategory("service"))
                .build();
    }
}