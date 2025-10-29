package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CalculationRequestDTO {

    @NotNull(message = "Giá xe không được để trống")
    @Positive(message = "Giá xe phải là số dương")
    private Long carPrice; // Giá xe (có thể là giá niêm yết hoặc giá ưu đãi)

    @NotNull(message = "Tỷ lệ trả trước không được để trống")
    @DecimalMin(value = "0.0", message = "Tỷ lệ trả trước phải >= 0")
    @DecimalMax(value = "1.0", message = "Tỷ lệ trả trước phải <= 1") // e.g., 0.3 for 30%
    private BigDecimal downPaymentRate;

    @NotNull(message = "Kỳ hạn vay không được để trống")
    @Positive(message = "Kỳ hạn vay phải là số dương")
    private Integer loanTermYears;

    // User can provide either a policy ID OR a custom rate
    private Integer policyId; // ID of the selected LoanPolicy

    @DecimalMin(value = "0.0", message = "Lãi suất phải >= 0")
    private BigDecimal customInterestRate; // Optional: Custom interest rate (% per year)

    // Validation: Ensure either policyId or customInterestRate is provided, not both/neither? (Can be done in Service)
}