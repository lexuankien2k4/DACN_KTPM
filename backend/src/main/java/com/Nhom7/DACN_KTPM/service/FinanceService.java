package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.request.CalculationRequestDTO;

import com.Nhom7.DACN_KTPM.dto.request.InstallmentPlanDTO;
import com.Nhom7.DACN_KTPM.dto.request.MonthlyPaymentDetail;
import com.Nhom7.DACN_KTPM.entity.LoanPolicy;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.repository.LoanPolicyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FinanceService {

    LoanPolicyRepository loanPolicyRepository;

    @Transactional(readOnly = true) // Read-only as it only calculates
    public InstallmentPlanDTO calculateLoanInstallment(CalculationRequestDTO request) {
        log.info("Calculation request received: {}", request);


        if (request.getPolicyId() == null && request.getCustomInterestRate() == null) {
            throw new AppException(ErrorCode.INVALID_CALCULATION_INPUT);
        }
        if (request.getPolicyId() != null && request.getCustomInterestRate() != null) {
            throw new AppException(ErrorCode.INVALID_CALCULATION_INPUT);
        }

        // --- Determine Interest Rate ---
        BigDecimal yearlyInterestRate;
        Integer preferentialMonths = 0;
        BigDecimal floatingMargin = BigDecimal.ZERO;
        // BigDecimal floatingBaseRate = ...; // In a real scenario, fetch the base rate (e.g., LSTK 12T)

        if (request.getPolicyId() != null) {
            LoanPolicy policy = loanPolicyRepository.findById(request.getPolicyId())
                    .orElseThrow(() -> new AppException(ErrorCode.LOAN_POLICY_NOT_FOUND));

            // Use preferential rate if available, otherwise consider margin (needs base rate logic)
            yearlyInterestRate = policy.getPreferentialRatePercent() != null ?
                    policy.getPreferentialRatePercent() :
                    BigDecimal.ZERO; // Simplified: Assume 0 if no pref rate, enhance later

            preferentialMonths = policy.getPreferentialRateMonths() != null ? policy.getPreferentialRateMonths() : 0;
            floatingMargin = policy.getFloatingRateMargin() != null ? policy.getFloatingRateMargin() : BigDecimal.ZERO;

            // Basic validation for term years against policy
            if(request.getLoanTermYears() > policy.getMaxLoanTermYears()){
                throw new AppException(ErrorCode.INVALID_CALCULATION_INPUT
                );
            }
            // Basic validation for down payment rate against policy
            if(request.getDownPaymentRate().compareTo(policy.getMinDownPaymentRate()) < 0){
                throw new AppException(ErrorCode.INVALID_CALCULATION_INPUT);
            }


        } else {
            yearlyInterestRate = request.getCustomInterestRate();
            // Custom rate implies no preferential period or floating margin from policy
        }

        BigDecimal monthlyInterestRate = yearlyInterestRate
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP) // Convert % to decimal
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP); // Convert yearly to monthly

        // --- Calculate Core Values ---
        long carPrice = request.getCarPrice();
        long downPaymentAmount = request.getDownPaymentRate().multiply(BigDecimal.valueOf(carPrice)).longValue();
        long loanPrincipal = carPrice - downPaymentAmount;
        int loanTermMonths = request.getLoanTermYears() * 12;

        if (loanPrincipal <= 0) {
            throw new AppException(ErrorCode.INVALID_CALCULATION_INPUT);
        }

        // --- Calculate Monthly Payment (Annuity Formula - simplified for fixed rate) ---
        // M = P * [r(1+r)^n] / [(1+r)^n – 1]
        // This calculates based ONLY on the initial rate. Handling floating rates needs iteration.
        double r = monthlyInterestRate.doubleValue();
        double p = loanPrincipal;
        int n = loanTermMonths;
        double monthlyPaymentDouble = (p * (r * Math.pow(1 + r, n))) / (Math.pow(1 + r, n) - 1);
        long estimatedMonthlyPayment = Math.round(monthlyPaymentDouble); // Simplified estimate

        // --- Generate Payment Schedule (Iterative Calculation) ---
        List<MonthlyPaymentDetail> schedule = new ArrayList<>();
        long remainingBalance = loanPrincipal;
        long totalInterest = 0;

        for (int month = 1; month <= loanTermMonths; month++) {
            // *** IMPORTANT: Logic to determine CURRENT monthly rate based on pref period ***
            BigDecimal currentMonthlyRate = monthlyInterestRate; // Simplified - Assume fixed rate for now
            // TODO: Enhance this logic:
            // if (month > preferentialMonths) {
            //     BigDecimal currentBaseRate = fetchCurrentBaseRate(); // Logic to get base rate
            //     BigDecimal currentYearlyFloatingRate = currentBaseRate.add(floatingMargin);
            //     currentMonthlyRate = currentYearlyFloatingRate.divide(...);
            // }

            long interestForMonth = BigDecimal.valueOf(remainingBalance)
                    .multiply(currentMonthlyRate)
                    .setScale(0, RoundingMode.HALF_UP).longValue();

            // Adjust last payment to clear balance exactly if using fixed M
            long principalForMonth;
            long paymentForThisMonth = estimatedMonthlyPayment; // Start with estimate
            if (month == loanTermMonths) {
                principalForMonth = remainingBalance;
                paymentForThisMonth = principalForMonth + interestForMonth; // Final payment adjusted
            } else {
                principalForMonth = estimatedMonthlyPayment - interestForMonth;
                // Handle cases where interest > estimated M (can happen with low P, high R)
                if (principalForMonth < 0) principalForMonth = 0;
            }
            // Ensure principal doesn't exceed remaining balance
            if (principalForMonth > remainingBalance) {
                principalForMonth = remainingBalance;
                paymentForThisMonth = principalForMonth + interestForMonth;
            }


            remainingBalance -= principalForMonth;
            totalInterest += interestForMonth;

            // Handle potential rounding issues on the very last payment
            if (month == loanTermMonths && remainingBalance != 0) {
                // Adjust the last principal/interest slightly if needed due to rounding
                long adjustment = -remainingBalance;
                principalForMonth += adjustment;
                paymentForThisMonth += adjustment;
                remainingBalance = 0;
            }


            schedule.add(MonthlyPaymentDetail.builder()
                    .month(month)
                    .interestPaid(interestForMonth)
                    .principalPaid(principalForMonth)
                    .totalPayment(paymentForThisMonth)
                    .remainingBalance(remainingBalance)
                    .interestRateApplied(currentMonthlyRate.multiply(BigDecimal.valueOf(100)).doubleValue()) // Monthly rate %
                    .build());

            if (remainingBalance < 0) {
                log.warn("Remaining balance went negative at month {}. Check calculations.", month);
                remainingBalance = 0; // Prevent further negative values
            }
        }

        // --- Build Response DTO ---
        return InstallmentPlanDTO.builder()
                .carPrice(carPrice)
                .downPaymentAmount(downPaymentAmount)
                .loanPrincipal(loanPrincipal)
                .loanTermMonths(loanTermMonths)
                .interestRateUsed(yearlyInterestRate) // Initial yearly rate used
                .totalInterestPaid(totalInterest)
                .totalAmountPaid(loanPrincipal + totalInterest)
                .estimatedMonthlyPayment(estimatedMonthlyPayment) // Provide the initial estimate
                .paymentSchedule(schedule)
                .build();
    }

}
