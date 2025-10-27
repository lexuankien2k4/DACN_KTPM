package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoanPolicyUpdateRequest {
    // Không cho phép đổi bankId

    @NotBlank(message = "Tên chính sách không được để trống")
    private String name;

    @NotNull(message = "Tỷ lệ trả trước tối thiểu không được để trống")
    @DecimalMin(value = "0.0", message = "Tỷ lệ trả trước phải lớn hơn hoặc bằng 0")
    private BigDecimal minDownPaymentRate;

    @NotNull(message = "Kỳ hạn vay tối đa không được để trống")
    @Positive(message = "Kỳ hạn vay phải là số dương")
    private Integer maxLoanTermYears;

    @DecimalMin(value = "0.0", message = "Lãi suất ưu đãi phải lớn hơn hoặc bằng 0")
    private BigDecimal preferentialRatePercent;

    @Positive(message = "Thời gian ưu đãi phải là số dương")
    private Integer preferentialRateMonths;

    @DecimalMin(value = "0.0", message = "Biên độ thả nổi phải lớn hơn hoặc bằng 0")
    private BigDecimal floatingRateMargin;


    private Boolean isActive;
}
