package service;

import model.Invoice;
import model.Payment;
import model.enums.PaymentStatus;
import repository.InvoiceRepository;
import repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InvoiceService {

    private static final BigDecimal VAT_RATE = BigDecimal.valueOf(0.20);

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }

    public Optional<Invoice> findById(Long id) {
        validateId(id);

        return invoiceRepository.findById(id);
    }

    public Optional<Invoice> findByPaymentId(Long paymentId) {
        validateId(paymentId);

        return invoiceRepository.findByPaymentId(paymentId);
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Invoice generateForPayment(Long paymentId) {
        validateId(paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment not found with id: " + paymentId
                ));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Invoice can only be generated for completed payment"
            );
        }

        if (invoiceRepository.findByPaymentId(paymentId).isPresent()) {
            throw new IllegalStateException(
                    "An invoice already exists for payment id: " + paymentId
            );
        }

        BigDecimal paymentAmount = payment.getAmount();

        if (paymentAmount == null) {
            throw new IllegalStateException(
                    "Payment amount cannot be null"
            );
        }

        if (paymentAmount.signum() <= 0) {
            throw new IllegalStateException(
                    "Payment amount must be greater than zero"
            );
        }

        /*
         * Payment amount is considered TTC.
         *
         * Example:
         * TTC = 1200
         * VAT = 20%
         *
         * HT  = 1200 / 1.20 = 1000
         * Tax = 1200 - 1000 = 200
         */
        BigDecimal totalTtc = paymentAmount.setScale(
                2,
                RoundingMode.HALF_UP
        );

        BigDecimal subtotalHt = totalTtc
                .divide(
                        BigDecimal.ONE.add(VAT_RATE),
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal taxAmount = totalTtc
                .subtract(subtotalHt)
                .setScale(2, RoundingMode.HALF_UP);

        Invoice invoice = new Invoice();

        invoice.setPaymentId(paymentId);
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setSubtotalHt(subtotalHt);
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalTtc(totalTtc);

        return invoiceRepository.save(invoice);
    }

    public void deleteById(Long id) {
        validateId(id);

        invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invoice not found with id: " + id
                ));

        /*
         * An invoice is a financial document.
         * It should not be deleted directly after being issued.
         */
        throw new IllegalStateException(
                "Invoices cannot be deleted directly"
        );
    }

    private String generateInvoiceNumber() {
        return "INV-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be greater than 0"
            );
        }
    }
}