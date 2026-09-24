package service;

import model.Payment;
import model.Reservation;
import model.enums.PaymentMethod;
import model.enums.PaymentStatus;
import model.enums.ReservationStatus;
import repository.PaymentRepository;
import repository.ReservationRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            ReservationRepository reservationRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    public Optional<Payment> findById(Long id) {

        validateId(id);

        return paymentRepository.findById(id);
    }

    public Optional<Payment> findByReservationId(Long reservationId) {

        validateId(reservationId);

        return paymentRepository.findByReservationId(reservationId);
    }

    public List<Payment> findByStatus(PaymentStatus status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Payment status cannot be null"
            );
        }

        return paymentRepository.findByStatus(status);
    }

    public Payment create(
            Long reservationId,
            BigDecimal amount,
            PaymentMethod method
    ) {

        validateId(reservationId);
        validateAmount(amount);

        if (method == null) {
            throw new IllegalArgumentException(
                    "Payment method cannot be null"
            );
        }

        Reservation reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Reservation not found with id: "
                                                + reservationId
                                )
                        );

        validateReservationForPayment(reservation);

        Optional<Payment> existingPayment =
                paymentRepository.findByReservationId(reservationId);

        if (existingPayment.isPresent()
                && existingPayment.get().getStatus()
                == PaymentStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Reservation is already fully paid"
            );
        }

        if (amount.compareTo(reservation.getTotalAmount()) != 0) {

            throw new IllegalArgumentException(
                    "Payment amount must match reservation total amount"
            );
        }

        Payment payment = new Payment();

        payment.setReservationId(reservationId);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.PENDING);

        return paymentRepository.save(payment);
    }

    public void complete(Long paymentId) {

        validateId(paymentId);

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found with id: "
                                                + paymentId
                                )
                        );

        if (payment.getStatus() != PaymentStatus.PENDING) {

            throw new IllegalStateException(
                    "Only pending payments can be completed"
            );
        }

        payment.setStatus(PaymentStatus.COMPLETED);

        paymentRepository.update(payment);
    }

    public void fail(Long paymentId) {

        validateId(paymentId);

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found with id: "
                                                + paymentId
                                )
                        );

        if (payment.getStatus() != PaymentStatus.PENDING) {

            throw new IllegalStateException(
                    "Only pending payments can be marked as failed"
            );
        }

        payment.setStatus(PaymentStatus.FAILED);

        paymentRepository.update(payment);
    }

    public void deleteById(Long paymentId) {

        validateId(paymentId);

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found with id: "
                                                + paymentId
                                )
                        );

        if (payment.getStatus() == PaymentStatus.COMPLETED
                || payment.getStatus() == PaymentStatus.REFUNDED) {

            throw new IllegalStateException(
                    "Completed or refunded payment cannot be deleted"
            );
        }

        paymentRepository.deleteById(paymentId);
    }

    private void validateReservationForPayment(
            Reservation reservation
    ) {

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Cancelled reservation cannot be paid"
            );
        }

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Completed reservation cannot receive a new payment"
            );
        }

        if (reservation.getTotalAmount() == null) {

            throw new IllegalStateException(
                    "Reservation total amount cannot be null"
            );
        }

        if (reservation.getTotalAmount().signum() <= 0) {

            throw new IllegalStateException(
                    "Reservation total amount must be greater than zero"
            );
        }
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null) {
            throw new IllegalArgumentException(
                    "Payment amount cannot be null"
            );
        }

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }
    }

    private void validateId(Long id) {

        if (id == null || id <= 0) {

            throw new IllegalArgumentException(
                    "ID must be greater than 0"
            );
        }
    }
}