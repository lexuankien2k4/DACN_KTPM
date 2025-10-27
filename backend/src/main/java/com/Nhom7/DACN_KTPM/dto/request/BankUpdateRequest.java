package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class BankUpdateRequest {

    @NotBlank(message = "Tên ngân hàng không được để trống")
    private String name;

    @URL(message = "URL logo không hợp lệ")
    private String logoUrl;

    private Boolean isActive; // Allow updating status
}