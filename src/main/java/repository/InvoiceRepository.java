package repository;


import model.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {
    Optional<Invoice> findById(Long id);
    Optional<Invoice> findByPaymentId(Long paymentId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findAll();
    Invoice save(Invoice invoice);
    void update(Invoice invoice);
    void deleteById(Long id);

}