package dto;

import java.math.BigDecimal;
import model.enums.RoomType;

public class AvailableRoomDTO {

    private Long roomId;
    private String roomNumber;
    private RoomType roomType;
    private Integer capacity;
    private BigDecimal basePrice;

    public AvailableRoomDTO() {}

    public AvailableRoomDTO(
            Long roomId,
            String roomNumber,
            RoomType roomType,
            Integer capacity,
            BigDecimal basePrice
    ) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}