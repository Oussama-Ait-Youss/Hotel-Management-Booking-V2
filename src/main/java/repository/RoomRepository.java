package repository;


import java.util.Optional;

import dto.RoomSearchCriteria;
import model.Room;
import java.util.List;

public interface RoomRepository {
    Optional<Room> findById(Long id);
    Optional<Room> findRoomNumber(String roomNumber);
    List<Room> findAll();
    List<Room> findAvailableRooms(RoomSearchCriteria criteris);
    public Room save(Room room);
    public void update(Room room);
    public void deleteById(Long id);



}