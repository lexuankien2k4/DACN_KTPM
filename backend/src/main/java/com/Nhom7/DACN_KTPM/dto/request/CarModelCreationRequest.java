package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarModelCreationRequest {

    @NotBlank(message = "Tên dòng xe không được để trống")
    private String name;

    private String bodyType;

    // Giả sử categoryId là bắt buộc khi tạo
    @NotNull(message = "Danh mục xe không được để trống")
    private Integer categoryId;

    private Boolean isActive = true; // Mặc định là active
}