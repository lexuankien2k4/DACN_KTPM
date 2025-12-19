package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.response.CarImageResponse; // Import DTO của bạn
import com.Nhom7.DACN_KTPM.entity.CarImage;
import com.Nhom7.DACN_KTPM.entity.CarVariant;
import com.Nhom7.DACN_KTPM.mapper.CarImageMapper; // Import Mapper của bạn
import com.Nhom7.DACN_KTPM.repository.CarImageRepository;
import com.Nhom7.DACN_KTPM.repository.CarVariantRepository;
import com.Nhom7.DACN_KTPM.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class CarImageController {

    private final FileStorageService fileStorageService;
    private final CarImageRepository carImageRepository;
    private final CarVariantRepository carVariantRepository;
    private final CarImageMapper carImageMapper; // Inject Mapper

    // API Upload ảnh
    @PostMapping("/upload/{variantId}")
    public ApiResponse<CarImageResponse> uploadImage(
            @PathVariable Integer variantId,
            @RequestParam("file") MultipartFile file
    ) {
        // 1. Kiểm tra variant tồn tại
        CarVariant variant = carVariantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên bản xe ID: " + variantId));

        // 2. Lưu file vật lý
        String imageUrl = fileStorageService.storeFile(file);

        // 3. Lưu vào DB
        CarImage carImage = new CarImage();
        carImage.setImageUrl(imageUrl);
        carImage.setCarVariant(variant);

        CarImage savedImage = carImageRepository.save(carImage);

        // 4. Dùng Mapper chuyển Entity sang DTO để trả về
        return ApiResponse.<CarImageResponse>builder()
                .result(carImageMapper.toCarImageResponse(savedImage))
                .message("Upload ảnh thành công")
                .build();
    }


    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteImage(@PathVariable Integer id) {
        if (!carImageRepository.existsById(id)) {
            throw new RuntimeException("Ảnh không tồn tại");
        }
        carImageRepository.deleteById(id);

        return ApiResponse.<String>builder()
                .result("Đã xóa ảnh thành công")
                .build();
    }
}