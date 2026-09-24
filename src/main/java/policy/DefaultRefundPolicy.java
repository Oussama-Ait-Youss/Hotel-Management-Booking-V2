package policy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DefaultRefundPolicy implements RefundPolicy {

    @Override
    public BigDecimal calculateRefundAmount(BigDecimal totalPaid, LocalDate checkIn, LocalDate cancelDate) {
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(cancelDate, checkIn);

        BigDecimal refundPercentage;

        if (daysBeforeCheckIn >= 14) {
            refundPercentage = new BigDecimal("1.00"); // 100%
        } else if (daysBeforeCheckIn >= 7) {
            refundPercentage = new BigDecimal("0.70"); // 70%
        } else if (daysBeforeCheckIn >= 2) { // 48h
            refundPercentage = new BigDecimal("0.50"); // 50%
        } else {
            refundPercentage = BigDecimal.ZERO; // 0%
        }

        return totalPaid.multiply(refundPercentage).setScale(2, RoundingMode.HALF_UP);
    }
}