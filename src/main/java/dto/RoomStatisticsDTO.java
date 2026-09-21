package dto;


import java.math.BigDecimal;
import model.enums.RoomType;



public class RoomStatisticsDTO {
    private RoomType roomType;
    private int totalRooms;
    private int occupiedRooms;
    private int availableRooms;
    private BigDecimal occupancyRate;
    private BigDecimal averagePrice;


    public RoomStatisticsDTO(){}
    public RoomStatisticsDTO(
            RoomType roomType,
            int totalRooms,
            int occupiedRooms,
            int availableRooms,
            BigDecimal occupancyRate,
            BigDecimal averagePrice
    ){
        this.roomType = roomType;
        this.totalRooms = totalRooms;
        this.occupiedRooms = occupiedRooms;
        this.availableRooms = availableRooms;
        this.occupancyRate = occupancyRate;
        this.averagePrice = averagePrice;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public int getOccupiedRooms() {
        return occupiedRooms;
    }

    public void setOccupiedRooms(int occupiedRooms) {
        this.occupiedRooms = occupiedRooms;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    public BigDecimal getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(BigDecimal occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }
}
