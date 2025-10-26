package com.Nhom7.DACN_KTPM.dto.response;

import lombok.Data;

@Data
public class CarVariantBasicResponse {
    private Long id;
    private String name; // Tên phiên bản
    private Long price;
    private Long discountedPrice;
    private Boolean isActive;
    private String modelName; // Tên dòng xe
    private String firstImageUrl; // Ảnh đại diện (ảnh đầu tiên)
}
