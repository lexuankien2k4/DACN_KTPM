package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CarCategoryCreationRequest {

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
}
