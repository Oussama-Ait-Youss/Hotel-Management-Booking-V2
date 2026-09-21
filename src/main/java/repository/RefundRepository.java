package repository;

import model.Refund;

import java.util.List;
import java.util.Optional;
import model.enums.RefundStatus;

public interface RefundRepository {
    Optional<Refund> findById(Long id);
    List<Refund> findByPaymentId(Long paymentId);
    List<Refund> findByStatus(RefundStatus status);
    List<Refund> findAll();
    Refund save(Refund refund);
    void update(Refund refund);
    void deleteById(Long id);
}
