package com.Nhom7.DACN_KTPM.dto.response;
import lombok.Data;


@Data
public class CarModelResponse {
    private Long id;
    private String name;
    private String bodyType;
    private Boolean isActive;
    private String categoryName;
}
