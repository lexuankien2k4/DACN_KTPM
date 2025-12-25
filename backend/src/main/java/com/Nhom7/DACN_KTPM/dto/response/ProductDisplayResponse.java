package com.Nhom7.DACN_KTPM.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDisplayResponse {
    private Long id;
    private String name;
    private Long price;
    private String image; // URL ảnh đại diện
    private String range; // Quãng đường
    private String seats; // Số chỗ
    private String type;  // Loại xe (SUV, Sedan...)
    private String categoryName;
}