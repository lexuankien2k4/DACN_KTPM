package com.Nhom7.DACN_KTPM.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "loan_policies")
public class LoanPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "min_down_payment_rate", nullable = false)
    private BigDecimal minDownPaymentRate;

    @Column(name = "max_loan_term_years", nullable = false)
    private Integer maxLoanTermYears;

    @Column(name = "preferential_rate_percent")
    private BigDecimal preferentialRatePercent;

    @Column(name = "preferential_rate_months")
    private Integer preferentialRateMonths;

    @Column(name = "floating_rate_margin")
    private BigDecimal floatingRateMargin;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;
}