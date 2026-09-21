package repository;


import java.util.Optional;

import dto.AvailableRoomDTO;
import dto.RoomSearchCriteria;
import model.Room;
import java.util.List;

public interface RoomRepository {
    Optional<Room> findById(Long id);
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findAll();
    List<AvailableRoomDTO> findAvailableRooms(RoomSearchCriteria criteris);
    public Room save(Room room);
    public void update(Room room);
    public void deleteById(Long id);



}