package policy;

import model.Room;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface PricingStrategy {
    BigDecimal calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut);
}