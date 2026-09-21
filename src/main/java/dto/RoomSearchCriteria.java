package dto;
import model.enums.RoomType;
import java.time.LocalDate;

public class RoomSearchCriteria {
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int guests;
    private RoomType roomType;


    public RoomSearchCriteria(){}

    public RoomSearchCriteria(
            LocalDate checkIn,
            LocalDate checkOut,
            int guests,
            RoomType roomType
    ){
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.roomType = roomType;
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

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
}
