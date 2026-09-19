package dto;


import java.math.BigDecimal;
import java.model.enums.RoomType;

public class AvailableRoomDTO {
    private Long id;
    private String roomNumber;
    private java.model.enums.RoomType type;
    private Integer capacity;
    private BigDecimal pricePerNight;
}
