package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CarModelUpdateRequest {

    @NotBlank(message = "Tên dòng xe không được để trống")
    private String name;

    private String bodyType;

    private Integer categoryId; // Cho phép cập nhật category

    private Boolean isActive; // Cho phép cập nhật trạng thái
}