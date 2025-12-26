package com.Nhom7.DACN_KTPM.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractCreationRequest {
    private Long depositId;
    private BigDecimal finalPrice;
    private String contractNumber;
    private String status;
}