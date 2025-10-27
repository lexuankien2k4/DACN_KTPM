package com.Nhom7.DACN_KTPM.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoanPolicyResponse {
    private Integer id;
    private String name;
    private Integer bankId;
    private String bankName; // Thêm tên ngân hàng
    private BigDecimal minDownPaymentRate;
    private Integer maxLoanTermYears;
    private BigDecimal preferentialRatePercent;
    private Integer preferentialRateMonths;
    private BigDecimal floatingRateMargin;
    private Boolean isActive;
}