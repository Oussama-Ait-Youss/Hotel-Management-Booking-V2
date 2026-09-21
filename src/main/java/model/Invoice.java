package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Invoice {

    private Long id;
    private Long paymentId;
    private String invoiceNumber;
    private BigDecimal subtotalHt;
    private BigDecimal taxAmount;
    private BigDecimal totalTtc;
    private LocalDateTime issuedAt;

    public Invoice() {
    }

    public Invoice(
            Long id,
            Long paymentId,
            String invoiceNumber,
            BigDecimal subtotalHt,
            BigDecimal taxAmount,
            BigDecimal totalTtc,
            LocalDateTime issuedAt
    ) {
        this.id = id;
        this.paymentId = paymentId;
        this.invoiceNumber = invoiceNumber;
        this.subtotalHt = subtotalHt;
        this.taxAmount = taxAmount;
        this.totalTtc = totalTtc;
        this.issuedAt = issuedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getSubtotalHt() {
        return subtotalHt;
    }

    public void setSubtotalHt(BigDecimal subtotalHt) {
        this.subtotalHt = subtotalHt;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalTtc() {
        return totalTtc;
    }

    public void setTotalTtc(BigDecimal totalTtc) {
        this.totalTtc = totalTtc;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
}