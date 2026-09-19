package dto;


import java.time.LocalDate;
import java.model.enums.ReservationStatus;
import java.math.BigDecimal;



public class ReservationSummaryDTO {
    private Long reservationId;
    private String customerName;
    private String roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status;
    private BigDecimal totalAmount;
}
