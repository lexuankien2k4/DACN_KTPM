package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class BankCreationRequest {

    @NotBlank(message = "Tên ngân hàng không được để trống")
    private String name;

    @URL(message = "URL logo không hợp lệ") // Validate if it's a URL
    private String logoUrl;

    private Boolean isActive = true; // Default to active
}
