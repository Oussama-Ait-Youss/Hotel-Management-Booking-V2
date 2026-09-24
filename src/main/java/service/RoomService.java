package service;

import dto.AvailableRoomDTO;
import dto.RoomSearchCriteria;
import model.Room;
import model.enums.RoomStatus;
import repository.RoomRepository;

import java.util.List;
import java.util.Optional;

public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // ==========================================
    // FIND
    // ==========================================

    public Optional<Room> findById(Long id) {

        validateId(id);

        return roomRepository.findById(id);
    }

    public Optional<Room> findByRoomNumber(String roomNumber) {

        validateRoomNumber(roomNumber);

        return roomRepository.findByRoomNumber(roomNumber);
    }

    public List<Room> findAll() {

        return roomRepository.findAll();
    }

    // ==========================================
    // CREATE
    // ==========================================

    public Room create(Room room) {

        // Validate room data
        validateRoom(room);

        // Check room number uniqueness
        if (roomRepository.findByRoomNumber(
                room.getRoomNumber()
        ).isPresent()) {

            throw new IllegalStateException(
                    "Room number already exists: "
                            + room.getRoomNumber()
            );
        }

        // Save room
        return roomRepository.save(room);
    }

    // ==========================================
    // UPDATE
    // ==========================================

    public void update(Room room) {

        // Validate room data
        validateRoom(room);

        // Validate ID
        validateId(room.getId());

        // Check that room exists
        Room existingRoom = roomRepository.findById(
                room.getId()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Room not found with id: "
                                + room.getId()
                )
        );

        // Check room number uniqueness
        Optional<Room> roomWithSameNumber =
                roomRepository.findByRoomNumber(
                        room.getRoomNumber()
                );

        /*
         * If another room already uses this room number,
         * reject the update.
         *
         * The same room keeping its own number is allowed.
         */
        if (roomWithSameNumber.isPresent()
                && !roomWithSameNumber.get()
                .getId()
                .equals(existingRoom.getId())) {

            throw new IllegalStateException(
                    "Room number already exists: "
                            + room.getRoomNumber()
            );
        }

        // Update room
        roomRepository.update(room);
    }

    // ==========================================
    // DELETE
    // ==========================================

    public void deleteById(Long id) {

        validateId(id);

        // Check that room exists
        if (roomRepository.findById(id).isEmpty()) {

            throw new IllegalArgumentException(
                    "Room not found with id: " + id
            );
        }

        // Delete room
        roomRepository.deleteById(id);
    }



    public void changeStatus(
            Long id,
            RoomStatus status
    ) {

        validateId(id);

        if (status == null) {
            throw new IllegalArgumentException(
                    "Room status cannot be null"
            );
        }

        // Find room
        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Room not found with id: " + id
                        )
                );

        // Change status
        room.setStatus(status);

        // Persist change
        roomRepository.update(room);
    }



    public List<AvailableRoomDTO> findAvailableRooms(
            RoomSearchCriteria criteria
    ) {

        validateSearchCriteria(criteria);

        return roomRepository.findAvailableRooms(criteria);
    }



    private void validateRoom(Room room) {

        if (room == null) {
            throw new IllegalArgumentException(
                    "Room cannot be null"
            );
        }

        validateIdIfPresent(room.getId());

        validateRoomNumber(room.getRoomNumber());

        if (room.getType() == null) {
            throw new IllegalArgumentException(
                    "Room type cannot be null"
            );
        }

        if (room.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Room status cannot be null"
            );
        }

        if (room.getBasePrice() == null) {
            throw new IllegalArgumentException(
                    "Room price cannot be null"
            );
        }

        if (room.getBasePrice().signum() < 0) {
            throw new IllegalArgumentException(
                    "Room price cannot be negative"
            );
        }
    }

    private void validateId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Room ID must be greater than 0"
            );
        }
    }

    private void validateIdIfPresent(Long id) {

        if (id != null && id <= 0) {
            throw new IllegalArgumentException(
                    "Room ID must be greater than 0"
            );
        }
    }

    private void validateRoomNumber(String roomNumber) {

        if (roomNumber == null
                || roomNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Room number cannot be empty"
            );
        }
    }

    private void validateSearchCriteria(
            RoomSearchCriteria criteria
    ) {

        if (criteria == null) {
            throw new IllegalArgumentException(
                    "Search criteria cannot be null"
            );
        }
    }
}