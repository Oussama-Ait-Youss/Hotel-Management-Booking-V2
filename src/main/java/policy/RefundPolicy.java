package policy;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RefundPolicy {
    BigDecimal calculateRefundAmount(BigDecimal totalPaid, LocalDate checkIn, LocalDate cancelDate);
}