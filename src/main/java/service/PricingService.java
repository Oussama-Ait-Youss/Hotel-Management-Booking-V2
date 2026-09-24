package service;

import model.Room;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PricingService {



    private static final BigDecimal WEEKEND_MULTIPLIER =
            new BigDecimal("1.10");

    private static final BigDecimal HIGH_SEASON_MULTIPLIER =
            new BigDecimal("1.20");



    public BigDecimal calculateBasePrice(Room room) {

        validateRoom(room);

        return room.getBasePrice()
                .setScale(2, RoundingMode.HALF_UP);
    }



    public BigDecimal calculatePrice(
            Room room,
            boolean weekend,
            boolean highSeason
    ) {

        validateRoom(room);

        BigDecimal price = room.getBasePrice();

        if (weekend) {
            price = price.multiply(WEEKEND_MULTIPLIER);
        }

        if (highSeason) {
            price = price.multiply(HIGH_SEASON_MULTIPLIER);
        }

        return price.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }



    public BigDecimal calculateTotalPrice(
            Room room,
            int numberOfNights,
            boolean weekend,
            boolean highSeason
    ) {

        validateRoom(room);
        validateNumberOfNights(numberOfNights);

        BigDecimal nightlyPrice = calculatePrice(
                room,
                weekend,
                highSeason
        );

        return nightlyPrice
                .multiply(
                        BigDecimal.valueOf(numberOfNights)
                )
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }



    public BigDecimal calculateWeekendPrice(Room room) {

        validateRoom(room);

        return room.getBasePrice()
                .multiply(WEEKEND_MULTIPLIER)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }



    public BigDecimal calculateHighSeasonPrice(Room room) {

        validateRoom(room);

        return room.getBasePrice()
                .multiply(HIGH_SEASON_MULTIPLIER)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }



    private void validateRoom(Room room) {

        if (room == null) {
            throw new IllegalArgumentException(
                    "Room cannot be null"
            );
        }

        if (room.getBasePrice() == null) {
            throw new IllegalArgumentException(
                    "Room base price cannot be null"
            );
        }

        if (room.getBasePrice().signum() < 0) {
            throw new IllegalArgumentException(
                    "Room base price cannot be negative"
            );
        }
    }

    private void validateNumberOfNights(
            int numberOfNights
    ) {

        if (numberOfNights <= 0) {
            throw new IllegalArgumentException(
                    "Number of nights must be greater than 0"
            );
        }
    }
}

