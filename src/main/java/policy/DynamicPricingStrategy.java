package policy;

import model.Room;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class DynamicPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut) {
        long totalNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal basePrice = room.getBasePrice();

        // 1. Calcul du prix nuit par nuit (Saisons et Week-ends)
        for (int i = 0; i < totalNights; i++) {
            LocalDate currentDate = checkIn.plusDays(i);
            BigDecimal nightPrice = basePrice;

            // Haute / Basse saison
            Month month = currentDate.getMonth();
            if (month == Month.JULY || month == Month.AUGUST) {
                nightPrice = nightPrice.multiply(new BigDecimal("1.30")); // +30%
            } else if (month == Month.NOVEMBER || month == Month.DECEMBER || month == Month.JANUARY || month == Month.FEBRUARY) {
                nightPrice = nightPrice.multiply(new BigDecimal("0.85")); // -15%
            }

            // Majoration Week-end (Vendredi et Samedi soir)
            DayOfWeek day = currentDate.getDayOfWeek();
            if (day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY) {
                nightPrice = nightPrice.multiply(new BigDecimal("1.15")); // +15%
            }

            totalPrice = totalPrice.add(nightPrice);
        }

        // 2. Modificateurs sur le montant total
        // Réservation anticipée ou dernière minute
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), checkIn);
        if (daysUntilCheckIn >= 30) {
            totalPrice = totalPrice.multiply(new BigDecimal("0.95")); // -5%
        } else if (daysUntilCheckIn <= 3) {
            totalPrice = totalPrice.multiply(new BigDecimal("1.10")); // +10%
        }

        // Séjours longs
        if (totalNights >= 14) {
            totalPrice = totalPrice.multiply(new BigDecimal("0.85")); // -15%
        } else if (totalNights >= 7) {
            totalPrice = totalPrice.multiply(new BigDecimal("0.90")); // -10%
        }

        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}