package service;

import model.Reservation;
import model.Room;
import model.enums.ReservationStatus;
import repository.ReservationRepository;
import repository.RoomRepository;
import repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            RoomRepository roomRepository,
            UserRepository userRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    // ==========================================
    // FIND BY ID
    // ==========================================

    public Optional<Reservation> findById(Long id) {

        validateId(id);

        return reservationRepository.findById(id);
    }



    public List<?> findByUserId(Long userId) {

        validateId(userId);

        return reservationRepository.findByUserId(userId);
    }


    public List<Reservation> findByRoomId(Long roomId) {

        validateId(roomId);

        return reservationRepository.findByRoomId(roomId);
    }



    public List<Reservation> findByStatus(
            ReservationStatus status
    ) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Reservation status cannot be null"
            );
        }

        return reservationRepository.findByStatus(status);
    }


    public Reservation create(
            Reservation reservation
    ) {

        validateReservation(reservation);

        // Validate user
        validateUser(reservation.getUserId());

        // Find room
        Room room = roomRepository.findById(
                reservation.getRoomId()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Room not found with id: "
                                + reservation.getRoomId()
                )
        );

        // Validate room
        validateRoomForReservation(room);

        // Validate dates
        validateDates(
                reservation.getCheckIn(),
                reservation.getCheckOut()
        );

        // Check availability
        if (reservationRepository.existsOverlappingReservation(
                reservation.getRoomId(),
                reservation.getCheckIn(),
                reservation.getCheckOut()
        )) {

            throw new IllegalStateException(
                    "Room is already reserved for the selected dates"
            );
        }

        // Calculate total amount
        BigDecimal totalAmount =
                calculateTotalAmount(
                        room,
                        reservation.getCheckIn(),
                        reservation.getCheckOut()
                );

        reservation.setTotalAmount(totalAmount);

        /*
         * New reservations start as PENDING.
         *
         * Payment/confirmation will be handled by the
         * corresponding business flow later.
         */
        if (reservation.getStatus() == null) {

            reservation.setStatus(
                    ReservationStatus.PENDING
            );
        }

        return reservationRepository.save(reservation);
    }


    public void update(
            Reservation reservation
    ) {

        validateReservation(reservation);

        validateId(reservation.getId());

        // Check reservation exists
        Reservation existingReservation =
                reservationRepository.findById(
                        reservation.getId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Reservation not found with id: "
                                        + reservation.getId()
                        )
                );

        // Validate user
        validateUser(reservation.getUserId());

        // Find room
        Room room = roomRepository.findById(
                reservation.getRoomId()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Room not found with id: "
                                + reservation.getRoomId()
                )
        );

        validateRoomForReservation(room);

        // Validate dates
        validateDates(
                reservation.getCheckIn(),
                reservation.getCheckOut()
        );

        /*
         * Check availability while excluding the current
         * reservation.
         *
         * The current repository method does not accept an
         * excluded reservation ID, so we manually check the
         * room's reservations below.
         */
        validateNoOverlapForUpdate(
                reservation,
                existingReservation
        );

        // Recalculate total amount
        BigDecimal totalAmount =
                calculateTotalAmount(
                        room,
                        reservation.getCheckIn(),
                        reservation.getCheckOut()
                );

        reservation.setTotalAmount(totalAmount);

        reservationRepository.update(reservation);
    }



    public void deleteById(Long id) {

        validateId(id);

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Reservation not found with id: "
                                                + id
                                )
                        );

        /*
         * A completed reservation should not be deleted.
         * Cancellation should be used instead.
         */
        if (reservation.getStatus()
                == ReservationStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Completed reservation cannot be deleted"
            );
        }

        reservationRepository.deleteById(id);
    }



    public void cancel(Long id) {

        validateId(id);

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Reservation not found with id: "
                                                + id
                                )
                        );

        ReservationStatus currentStatus =
                reservation.getStatus();

        if (currentStatus
                == ReservationStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Reservation is already cancelled"
            );
        }

        if (currentStatus
                == ReservationStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Completed reservation cannot be cancelled"
            );
        }

        reservation.setStatus(
                ReservationStatus.CANCELLED
        );

        reservationRepository.update(reservation);
    }



    public void confirm(Long id) {

        validateId(id);

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Reservation not found with id: "
                                                + id
                                )
                        );

        if (reservation.getStatus()
                != ReservationStatus.PENDING) {

            throw new IllegalStateException(
                    "Only pending reservations can be confirmed"
            );
        }

        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );

        reservationRepository.update(reservation);
    }



    public void complete(Long id) {

        validateId(id);

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Reservation not found with id: "
                                                + id
                                )
                        );

        if (reservation.getStatus()
                != ReservationStatus.CONFIRMED) {

            throw new IllegalStateException(
                    "Only confirmed reservations can be completed"
            );
        }

        reservation.setStatus(
                ReservationStatus.COMPLETED
        );

        reservationRepository.update(reservation);
    }



    public long calculateNumberOfNights(
            LocalDate checkIn,
            LocalDate checkOut
    ) {

        validateDates(checkIn, checkOut);

        return ChronoUnit.DAYS.between(
                checkIn,
                checkOut
        );
    }



    public BigDecimal calculateTotalAmount(
            Room room,
            LocalDate checkIn,
            LocalDate checkOut
    ) {

        if (room == null) {
            throw new IllegalArgumentException(
                    "Room cannot be null"
            );
        }

        if (room.getBasePrice() == null) {
            throw new IllegalArgumentException(
                    "Room base price cannot be null"
            );
        }

        if (room.getBasePrice().signum() < 0) {
            throw new IllegalArgumentException(
                    "Room base price cannot be negative"
            );
        }

        long numberOfNights =
                calculateNumberOfNights(
                        checkIn,
                        checkOut
                );

        return room.getBasePrice()
                .multiply(
                        BigDecimal.valueOf(numberOfNights)
                );
    }



    private void validateReservation(
            Reservation reservation
    ) {

        if (reservation == null) {
            throw new IllegalArgumentException(
                    "Reservation cannot be null"
            );
        }

        if (reservation.getUserId() == null) {
            throw new IllegalArgumentException(
                    "User ID cannot be null"
            );
        }

        if (reservation.getRoomId() == null) {
            throw new IllegalArgumentException(
                    "Room ID cannot be null"
            );
        }

        if (reservation.getCheckIn() == null) {
            throw new IllegalArgumentException(
                    "Check-in date cannot be null"
            );
        }

        if (reservation.getCheckOut() == null) {
            throw new IllegalArgumentException(
                    "Check-out date cannot be null"
            );
        }

        if (reservation.getStatus() != null
                && !isValidStatus(
                reservation.getStatus()
        )) {

            throw new IllegalArgumentException(
                    "Invalid reservation status"
            );
        }
    }

    private void validateDates(
            LocalDate checkIn,
            LocalDate checkOut
    ) {

        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException(
                    "Check-in and check-out dates cannot be null"
            );
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Check-in date cannot be in the past"
            );
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }
    }

    private void validateUser(Long userId) {

        validateId(userId);

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with id: "
                                        + userId
                        )
                );
    }

    private void validateRoomForReservation(
            Room room
    ) {

        if (room.getStatus() == null) {
            throw new IllegalStateException(
                    "Room status cannot be null"
            );
        }

        if (room.getStatus()
                == model.enums.RoomStatus.MAINTENANCE) {

            throw new IllegalStateException(
                    "Room is currently under maintenance"
            );
        }
    }

    private void validateNoOverlapForUpdate(
            Reservation reservation,
            Reservation existingReservation
    ) {

        List<Reservation> roomReservations =
                reservationRepository.findByRoomId(
                        reservation.getRoomId()
                );

        for (Reservation other : roomReservations) {

            // Ignore the reservation being updated
            if (other.getId().equals(
                    existingReservation.getId()
            )) {
                continue;
            }

            // Only active reservations block the room
            if (other.getStatus()
                    != ReservationStatus.PENDING
                    && other.getStatus()
                    != ReservationStatus.CONFIRMED) {

                continue;
            }

            boolean overlaps =
                    reservation.getCheckIn()
                            .isBefore(other.getCheckOut())
                            && reservation.getCheckOut()
                            .isAfter(other.getCheckIn());

            if (overlaps) {

                throw new IllegalStateException(
                        "Room is already reserved for the selected dates"
                );
            }
        }
    }

    private boolean isValidStatus(
            ReservationStatus status
    ) {

        return status == ReservationStatus.PENDING
                || status == ReservationStatus.CONFIRMED
                || status == ReservationStatus.CANCELLED
                || status == ReservationStatus.COMPLETED;
    }

    private void validateId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be greater than 0"
            );
        }
    }
}
