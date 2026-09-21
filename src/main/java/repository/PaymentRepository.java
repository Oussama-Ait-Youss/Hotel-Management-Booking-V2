package repository;


import com.sun.source.tree.LiteralTree;
import model.Payment;

import javax.swing.text.html.Option;
import java.sql.ClientInfoStatus;
import java.util.List;
import java.util.Optional;
import model.Payment;
import model.enums.PaymentStatus;

public interface PaymentRepository {
    Optional<Payment> findById(Long id);
    Optional<Payment> findByReservationId(Long reservationId);
    List<Payment> findAll();
    List<Payment> findByStatus(PaymentStatus status);
    Payment save(Payment payment);
    void update (Payment payment);
    void deleteById(Long id);
}