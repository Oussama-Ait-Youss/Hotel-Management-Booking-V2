package dto;


import java.time.LocalDate;
import model.enums.ReservationStatus;
import java.math.BigDecimal;



public class ReservationSummaryDTO {
    private Long reservationId;
    private String customerName;
    private String roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status;
    private BigDecimal totalAmount;

    public ReservationSummaryDTO(){}
    public ReservationSummaryDTO(
            Long reservationId, String customerName,
            String roomNumber,
            LocalDate checkIn,
            LocalDate checkOut,
            ReservationStatus status,
            BigDecimal totalAmount){
            this.reservationId = reservationId;
            this.customerName = customerName;
            this.roomNumber = roomNumber;
            this.checkIn = checkOut;
            this.status = status;
            this.totalAmount = totalAmount;

    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
