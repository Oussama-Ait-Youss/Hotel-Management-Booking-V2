package dto;


import java.math.BigDecimal;
import java.model.enums.RoomType;

public class AvailableRoomDTO {
    private Long id;
    private String roomNumber;
    private RoomType type;
    private Integer capacity;
    private BigDecimal pricePerNight;



    public AvailableRoomDTO(){}
    public AvailableRoomDTO(
            Long id,
            String roomNumber,
            RoomType type,
            Integer capacity,
            BigDecimal pricePerNight
    ){
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
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

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}
