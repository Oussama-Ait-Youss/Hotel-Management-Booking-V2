package service;

import model.Payment;
import model.Refund;
import model.Reservation;
import model.enums.PaymentStatus;
import model.enums.RefundStatus;
import model.enums.ReservationStatus;
import repository.PaymentRepository;
import repository.RefundRepository;
import repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public RefundService(
            RefundRepository refundRepository,
            PaymentRepository paymentRepository,
            ReservationRepository reservationRepository
    ) {
        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    // =========================================================
    // FIND
    // =========================================================

    public Optional<Refund> findById(Long id) {

        validateId(id);

        return refundRepository.findById(id);
    }

    public List<Refund> findByPaymentId(Long paymentId) {

        validateId(paymentId);

        return refundRepository.findByPaymentId(paymentId);
    }

    public List<Refund> findByStatus(RefundStatus status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Refund status cannot be null"
            );
        }

        return refundRepository.findByStatus(status);
    }

    public List<Refund> findAll() {

        return refundRepository.findAll();
    }

    // =========================================================
    // CREATE REFUND
    // =========================================================

    public Refund createRefund(Long paymentId) {

        validateId(paymentId);

        // 1. Payment must exist
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Payment not found with id: " + paymentId
                        )
                );

        // 2. Payment must be completed
        if (payment.getStatus() != PaymentStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Only completed payments can be refunded"
            );
        }

        // 3. A payment cannot have multiple refunds
        List<Refund> existingRefunds =
                refundRepository.findByPaymentId(paymentId);

        if (!existingRefunds.isEmpty()) {

            throw new IllegalStateException(
                    "A refund already exists for payment id: "
                            + paymentId
            );
        }

        // 4. Reservation must exist
        Reservation reservation =
                reservationRepository.findById(
                        payment.getReservationId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Reservation not found with id: "
                                        + payment.getReservationId()
                        )
                );

        // 5. Reservation must be cancelled
        if (reservation.getStatus()
                != ReservationStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Only cancelled reservations can be refunded"
            );
        }

        // 6. Calculate refund amount
        BigDecimal refundAmount =
                calculateRefundAmount(
                        payment.getAmount(),
                        reservation.getCheckIn(),
                        LocalDate.now()
                );

        // 7. Create refund
        Refund refund = new Refund();

        refund.setPaymentId(paymentId);
        refund.setAmount(refundAmount);
        refund.setStatus(RefundStatus.PENDING);

        refund.setReason(
                buildRefundReason(refundAmount, payment.getAmount())
        );

        return refundRepository.save(refund);
    }

    // =========================================================
    // COMPLETE REFUND
    // =========================================================

    public void complete(Long refundId) {

        validateId(refundId);

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Refund not found with id: " + refundId
                        )
                );

        // Only PENDING refunds can be completed
        if (refund.getStatus() != RefundStatus.PENDING) {

            throw new IllegalStateException(
                    "Only pending refunds can be completed"
            );
        }

        // Update refund
        refund.setStatus(RefundStatus.COMPLETED);
        refund.setProcessedAt(LocalDateTime.now());

        refundRepository.update(refund);

        // Update payment status
        Payment payment =
                paymentRepository.findById(
                        refund.getPaymentId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Payment not found with id: "
                                        + refund.getPaymentId()
                        )
                );

        payment.setStatus(PaymentStatus.REFUNDED);

        paymentRepository.update(payment);
    }

    // =========================================================
    // FAIL REFUND
    // =========================================================

    public void fail(Long refundId) {

        validateId(refundId);

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Refund not found with id: " + refundId
                        )
                );

        // Only PENDING refunds can fail
        if (refund.getStatus() != RefundStatus.PENDING) {

            throw new IllegalStateException(
                    "Only pending refunds can be marked as failed"
            );
        }

        refund.setStatus(RefundStatus.FAILED);
        refund.setProcessedAt(LocalDateTime.now());

        refundRepository.update(refund);
    }

    // =========================================================
    // CALCULATE REFUND
    // =========================================================

    public BigDecimal calculateRefundAmount(
            BigDecimal paymentAmount,
            LocalDate checkIn,
            LocalDate cancellationDate
    ) {

        // Payment amount validation
        if (paymentAmount == null) {

            throw new IllegalArgumentException(
                    "Payment amount cannot be null"
            );
        }

        if (paymentAmount.signum() < 0) {

            throw new IllegalArgumentException(
                    "Payment amount cannot be negative"
            );
        }

        // Check-in validation
        if (checkIn == null) {

            throw new IllegalArgumentException(
                    "Check-in date cannot be null"
            );
        }

        // Cancellation date validation
        if (cancellationDate == null) {

            throw new IllegalArgumentException(
                    "Cancellation date cannot be null"
            );
        }

        // Cancellation cannot happen after check-in
        if (cancellationDate.isAfter(checkIn)) {

            throw new IllegalArgumentException(
                    "Cancellation date cannot be after check-in date"
            );
        }

        long daysBeforeCheckIn =
                ChronoUnit.DAYS.between(
                        cancellationDate,
                        checkIn
                );

        /*
         * Refund policy:
         *
         * >= 7 days before check-in → 100%
         * 3 to 6 days               → 50%
         * < 3 days                  → 0%
         */

        if (daysBeforeCheckIn >= 7) {

            return paymentAmount;
        }

        if (daysBeforeCheckIn >= 3) {

            return paymentAmount
                    .multiply(BigDecimal.valueOf(0.50));
        }

        return BigDecimal.ZERO;
    }

    // =========================================================
    // REFUND REASON
    // =========================================================

    private String buildRefundReason(
            BigDecimal refundAmount,
            BigDecimal paymentAmount
    ) {

        if (refundAmount.compareTo(paymentAmount) == 0) {

            return "Full refund";
        }

        if (refundAmount.signum() == 0) {

            return "No refund according to cancellation policy";
        }

        return "Partial refund according to cancellation policy";
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateId(Long id) {

        if (id == null || id <= 0) {

            throw new IllegalArgumentException(
                    "ID must be greater than 0"
            );
        }
    }
}