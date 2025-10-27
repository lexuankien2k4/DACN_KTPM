package com.Nhom7.DACN_KTPM.dto.response;
import lombok.Data;

@Data
public class BankResponse {
    private Integer id; // Matches INT in DB
    private String name;
    private String logoUrl;
    private Boolean isActive;
}