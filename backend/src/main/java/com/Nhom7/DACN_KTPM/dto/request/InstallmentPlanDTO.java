package com.Nhom7.DACN_KTPM.dto.request;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder // Use Builder pattern for easy construction
public class InstallmentPlanDTO {
    private long carPrice;
    private long downPaymentAmount;
    private long loanPrincipal; // Số tiền vay gốc
    private int loanTermMonths;
    private BigDecimal interestRateUsed; // Lãi suất thực tế được dùng (% năm)
    private long totalInterestPaid; // Tổng lãi dự kiến
    private long totalAmountPaid; // Tổng gốc + lãi dự kiến
    private long estimatedMonthlyPayment; // Số tiền trả hàng tháng (có thể thay đổi nếu lãi suất thả nổi)
    private List<MonthlyPaymentDetail> paymentSchedule; // Lịch trả nợ chi tiết
}