package com.Nhom7.DACN_KTPM.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthlyPaymentDetail {
    private int month; // Kỳ trả nợ thứ mấy
    private long principalPaid; // Gốc trả kỳ này
    private long interestPaid; // Lãi trả kỳ này
    private long totalPayment; // Tổng trả kỳ này (Gốc + Lãi)
    private long remainingBalance; // Dư nợ cuối kỳ
    private double interestRateApplied; // Lãi suất áp dụng cho kỳ này (% tháng)
}
