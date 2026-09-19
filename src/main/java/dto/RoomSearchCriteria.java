package dto;
import java.model.enums.RoomType;
import java.time.LocalDate;

public class RoomSearchCriteria {
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int guests;
    private RoomType roomType;
}
