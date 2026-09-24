package service;

import db.DatabaseConnection;
import model.*;
import model.enums.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class BookingTransactionService {

    public void processFullBooking(User user, Room room, LocalDate checkIn, LocalDate checkOut, BigDecimal totalAmount, PaymentMethod method) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // DÉBUT DE LA TRANSACTION ACID

            // 1. Insérer la réservation
            String resSql = "INSERT INTO reservations (user_id, room_id, check_in, check_out, status, total_amount) VALUES (?, ?, ?, ?, ?::reservation_status, ?) RETURNING id";
            long reservationId;
            try (PreparedStatement stmt = conn.prepareStatement(resSql)) {
                stmt.setLong(1, user.getId());
                stmt.setLong(2, room.getId());
                stmt.setDate(3, java.sql.Date.valueOf(checkIn));
                stmt.setDate(4, java.sql.Date.valueOf(checkOut));
                stmt.setString(5, ReservationStatus.CONFIRMED.name());
                stmt.setBigDecimal(6, totalAmount);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                reservationId = rs.getLong(1);
            }

            // 2. Insérer le paiement
            String paySql = "INSERT INTO payments (reservation_id, amount, method, status, paid_at) VALUES (?, ?, ?::payment_method, ?::payment_status, NOW()) RETURNING id";
            long paymentId;
            try (PreparedStatement stmt = conn.prepareStatement(paySql)) {
                stmt.setLong(1, reservationId);
                stmt.setBigDecimal(2, totalAmount);
                stmt.setString(3, method.name());
                stmt.setString(4, PaymentStatus.COMPLETED.name());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                paymentId = rs.getLong(1);
            }

            // 3. Insérer la facture
            String invSql = "INSERT INTO invoices (payment_id, invoice_number, subtotal_ht, tax_amount, total_ttc) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(invSql)) {
                BigDecimal subtotalHt = totalAmount.divide(new BigDecimal("1.20"), 2, java.math.RoundingMode.HALF_UP);
                BigDecimal tax = totalAmount.subtract(subtotalHt);

                stmt.setLong(1, paymentId);
                stmt.setString(2, "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                stmt.setBigDecimal(3, subtotalHt);
                stmt.setBigDecimal(4, tax);
                stmt.setBigDecimal(5, totalAmount);
                stmt.executeUpdate();
            }

            // 4. Mettre à jour le statut de la chambre
            String roomSql = "UPDATE rooms SET status = ?::room_status WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(roomSql)) {
                stmt.setString(1, RoomStatus.OCCUPIED.name());
                stmt.setLong(2, room.getId());
                stmt.executeUpdate();
            }

            conn.commit(); // VALIDATION DE LA TRANSACTION
            System.out.println(" Réservation, Paiement et Facture créés avec succès !");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // ANNULATION COMPLÈTE EN CAS D'ERREUR
                    System.err.println(" Transaction annulée (Rollback) : " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}