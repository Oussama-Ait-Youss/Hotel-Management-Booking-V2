package repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dto.ReservationSummaryDTO;
import model.Reservation;



public interface ReservationRepository {
    Optional<Reservation> findById(Long id);
    List<ReservationSummaryDTO> findByUserId(Long userId);
    List<Reservation> findByRoomId(Long roomId);
    public boolean existsOverlappingReservation(
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut
    );
    public Reservation save(Reservation reservation);
    void update(Reservation reservation);
    void delete(Long id);
}