package model;

import model.enums.RoomStatus;
import model.enums.RoomType;

import java.math.BigDecimal;

public class Room {

    private Long id;
    private String roomNumber;
    private RoomType type;
    private Integer capacity;
    private BigDecimal basePrice;
    private RoomStatus status;
    private String description;

    public Room() {
    }

    public Room(
            Long id,
            String roomNumber,
            RoomType type,
            Integer capacity,
            BigDecimal basePrice,
            RoomStatus status,
            String description
    ) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.status = status;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
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

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}