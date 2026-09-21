package repository.jdbc;

import db.DatabaseConnection;
import model.Invoice;
import model.Payment;
import model.enums.PaymentMethod;
import model.enums.PaymentStatus;
import repository.InvoiceRepository;
import repository.PaymentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPaymentRepository implements PaymentRepository {

    private final DatabaseConnection databaseConnection;

    public JdbcPaymentRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    @Override
    public Optional<Payment> findById(Long id) {

        String sql = """
            SELECT id,
                   reservation_id,
                   amount,
                   method,
                   status,
                   paid_at
            FROM payments
            WHERE id = ?
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Payment payment =
                        mapResultSetToPayment(resultSet);

                return Optional.of(payment);
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding payment with id: " + id,
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing ResultSet: "
                                    + e.getMessage()
                    );
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: "
                                    + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public Optional<Payment> findByReservationId(
            Long reservationId
    ) {

        String sql = """
            SELECT id,
                   reservation_id,
                   amount,
                   method,
                   status,
                   paid_at
            FROM payments
            WHERE reservation_id = ?
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, reservationId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Payment payment =
                        mapResultSetToPayment(resultSet);

                return Optional.of(payment);
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding payment for reservation: "
                            + reservationId,
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing ResultSet: "
                                    + e.getMessage()
                    );
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: "
                                    + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public List<Payment> findAll() {

        String sql = """
            SELECT id,
                   reservation_id,
                   amount,
                   method,
                   status,
                   paid_at
            FROM payments
            ORDER BY id
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Payment> payments = new ArrayList<>();

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Payment payment =
                        mapResultSetToPayment(resultSet);

                payments.add(payment);
            }

            return payments;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding all payments",
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing ResultSet: "
                                    + e.getMessage()
                    );
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: "
                                    + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public List<Payment> findByStatus(
            PaymentStatus status
    ) {

        String sql = """
            SELECT id,
                   reservation_id,
                   amount,
                   method,
                   status,
                   paid_at
            FROM payments
            WHERE status = ?::payment_status
            ORDER BY id
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Payment> payments = new ArrayList<>();

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setString(1, status.name());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Payment payment =
                        mapResultSetToPayment(resultSet);

                payments.add(payment);
            }

            return payments;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding payments with status: "
                            + status,
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing ResultSet: "
                                    + e.getMessage()
                    );
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: "
                                    + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public Payment save(Payment payment) {

        String sql = """
            INSERT INTO payments (
                reservation_id,
                amount,
                method,
                status,
                paid_at
            )
            VALUES (
                ?, ?, ?::payment_method, ?::payment_status, ?
            )
            RETURNING id
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(
                    1,
                    payment.getReservationId()
            );

            statement.setBigDecimal(
                    2,
                    payment.getAmount()
            );

            statement.setString(
                    3,
                    payment.getMethod().name()
            );

            statement.setString(
                    4,
                    payment.getStatus().name()
            );

            if (payment.getPaidAt() != null) {
                statement.setObject(
                        5,
                        payment.getPaidAt()
                );
            } else {
                statement.setObject(5, null);
            }

            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                payment.setId(
                        resultSet.getLong("id")
                );

                return payment;
            }

            throw new RuntimeException(
                    "Failed to save payment"
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error saving payment",
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing ResultSet: "
                                    + e.getMessage()
                    );
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: "
                                    + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public void update(Payment payment) {

        String sql = """
            UPDATE payments
            SET reservation_id = ?,
                amount = ?,
                method = ?::payment_method,
                status = ?::payment_status,
                paid_at = ?
            WHERE id = ?
            """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(
                    1,
                    payment.getReservationId()
            );

            statement.setBigDecimal(
                    2,
                    payment.getAmount()
            );

            statement.setString(
                    3,
                    payment.getMethod().name()
            );

            statement.setString(
                    4,
                    payment.getStatus().name()
            );

            if (payment.getPaidAt() != null) {
                statement.setObject(
                        5,
                        payment.getPaidAt()
                );
            } else {
                statement.setObject(5, null);
            }

            statement.setLong(
                    6,
                    payment.getId()
            );

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {

                throw new RuntimeException(
                        "Payment not found with id: "
                                + payment.getId()
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating payment with id: "
                            + payment.getId(),
                    e
            );

        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: "
                                    + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public void deleteById(Long id) {

        String sql = """
            DELETE FROM payments
            WHERE id = ?
            """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {

                throw new RuntimeException(
                        "Payment not found with id: " + id
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error deleting payment with id: " + id,
                    e
            );

        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: "
                                    + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    private Payment mapResultSetToPayment(
            ResultSet resultSet
    ) throws SQLException {

        Payment payment = new Payment();

        payment.setId(
                resultSet.getLong("id")
        );

        payment.setReservationId(
                resultSet.getLong("reservation_id")
        );

        payment.setAmount(
                resultSet.getBigDecimal("amount")
        );

        payment.setMethod(
                PaymentMethod.valueOf(
                        resultSet.getString("method")
                )
        );

        payment.setStatus(
                PaymentStatus.valueOf(
                        resultSet.getString("status")
                )
        );

        payment.setPaidAt(
                resultSet.getObject(
                        "paid_at",
                        LocalDateTime.class
                )
        );

        return payment;
    }

    public static class JdbcInvoiceRepository implements InvoiceRepository {

        private final DatabaseConnection databaseConnection;

        public JdbcInvoiceRepository() {
            this.databaseConnection = DatabaseConnection.getInstance();
        }

        @Override
        public Optional<Invoice> findById(Long id) {

            String sql = """
                SELECT id,
                       payment_id,
                       invoice_number,
                       subtotal_ht,
                       tax_amount,
                       total_ttc,
                       issued_at
                FROM invoices
                WHERE id = ?
                """;

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {

                connection = databaseConnection.getConnection();
                statement = connection.prepareStatement(sql);

                statement.setLong(1, id);

                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    Invoice invoice =
                            mapResultSetToInvoice(resultSet);

                    return Optional.of(invoice);
                }

                return Optional.empty();

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Error finding invoice with id: " + id,
                        e
                );

            } finally {

                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing ResultSet: "
                                        + e.getMessage()
                        );
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing PreparedStatement: "
                                        + e.getMessage()
                        );
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing Connection: "
                                        + e.getMessage()
                        );
                    }
                }
            }
        }

        @Override
        public Optional<Invoice> findByPaymentId(
                Long paymentId
        ) {

            String sql = """
                SELECT id,
                       payment_id,
                       invoice_number,
                       subtotal_ht,
                       tax_amount,
                       total_ttc,
                       issued_at
                FROM invoices
                WHERE payment_id = ?
                """;

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {

                connection = databaseConnection.getConnection();
                statement = connection.prepareStatement(sql);

                statement.setLong(1, paymentId);

                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    Invoice invoice =
                            mapResultSetToInvoice(resultSet);

                    return Optional.of(invoice);
                }

                return Optional.empty();

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Error finding invoice for payment: "
                                + paymentId,
                        e
                );

            } finally {

                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing ResultSet: "
                                        + e.getMessage()
                        );
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing PreparedStatement: "
                                        + e.getMessage()
                        );
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing Connection: "
                                        + e.getMessage()
                        );
                    }
                }
            }
        }

        @Override
        public Optional<Invoice> findByInvoiceNumber(
                String invoiceNumber
        ) {

            String sql = """
                SELECT id,
                       payment_id,
                       invoice_number,
                       subtotal_ht,
                       tax_amount,
                       total_ttc,
                       issued_at
                FROM invoices
                WHERE invoice_number = ?
                """;

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {

                connection = databaseConnection.getConnection();
                statement = connection.prepareStatement(sql);

                statement.setString(1, invoiceNumber);

                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    Invoice invoice =
                            mapResultSetToInvoice(resultSet);

                    return Optional.of(invoice);
                }

                return Optional.empty();

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Error finding invoice with number: "
                                + invoiceNumber,
                        e
                );

            } finally {

                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing ResultSet: "
                                        + e.getMessage()
                        );
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing PreparedStatement: "
                                        + e.getMessage()
                        );
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing Connection: "
                                        + e.getMessage()
                        );
                    }
                }
            }
        }

        @Override
        public List<Invoice> findAll() {

            String sql = """
                SELECT id,
                       payment_id,
                       invoice_number,
                       subtotal_ht,
                       tax_amount,
                       total_ttc,
                       issued_at
                FROM invoices
                ORDER BY id
                """;

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            List<Invoice> invoices = new ArrayList<>();

            try {

                connection = databaseConnection.getConnection();
                statement = connection.prepareStatement(sql);

                resultSet = statement.executeQuery();

                while (resultSet.next()) {

                    Invoice invoice =
                            mapResultSetToInvoice(resultSet);

                    invoices.add(invoice);
                }

                return invoices;

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Error finding all invoices",
                        e
                );

            } finally {

                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing ResultSet: "
                                        + e.getMessage()
                        );
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing PreparedStatement: "
                                        + e.getMessage()
                        );
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing Connection: "
                                        + e.getMessage()
                        );
                    }
                }
            }
        }

        @Override
        public Invoice save(Invoice invoice) {

            String sql = """
                INSERT INTO invoices (
                    payment_id,
                    invoice_number,
                    subtotal_ht,
                    tax_amount,
                    total_ttc
                )
                VALUES (?, ?, ?, ?, ?)
                RETURNING id, issued_at
                """;

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {

                connection = databaseConnection.getConnection();
                statement = connection.prepareStatement(sql);

                statement.setLong(
                        1,
                        invoice.getPaymentId()
                );

                statement.setString(
                        2,
                        invoice.getInvoiceNumber()
                );

                statement.setBigDecimal(
                        3,
                        invoice.getSubtotalHt()
                );

                statement.setBigDecimal(
                        4,
                        invoice.getTaxAmount()
                );

                statement.setBigDecimal(
                        5,
                        invoice.getTotalTtc()
                );

                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    invoice.setId(
                            resultSet.getLong("id")
                    );

                    invoice.setIssuedAt(
                            resultSet.getObject(
                                    "issued_at",
                                    LocalDateTime.class
                            )
                    );

                    return invoice;
                }

                throw new RuntimeException(
                        "Failed to save invoice"
                );

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Error saving invoice",
                        e
                );

            } finally {

                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing ResultSet: "
                                        + e.getMessage()
                        );
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing PreparedStatement: "
                                        + e.getMessage()
                        );
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing Connection: "
                                        + e.getMessage()
                        );
                    }
                }
            }
        }

        @Override
        public void update(Invoice invoice) {

            String sql = """
                UPDATE invoices
                SET payment_id = ?,
                    invoice_number = ?,
                    subtotal_ht = ?,
                    tax_amount = ?,
                    total_ttc = ?
                WHERE id = ?
                """;

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = databaseConnection.getConnection();
                statement = connection.prepareStatement(sql);

                statement.setLong(
                        1,
                        invoice.getPaymentId()
                );

                statement.setString(
                        2,
                        invoice.getInvoiceNumber()
                );

                statement.setBigDecimal(
                        3,
                        invoice.getSubtotalHt()
                );

                statement.setBigDecimal(
                        4,
                        invoice.getTaxAmount()
                );

                statement.setBigDecimal(
                        5,
                        invoice.getTotalTtc()
                );

                statement.setLong(
                        6,
                        invoice.getId()
                );

                int rowsAffected =
                        statement.executeUpdate();

                if (rowsAffected == 0) {

                    throw new RuntimeException(
                            "Invoice not found with id: "
                                    + invoice.getId()
                    );
                }

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Error updating invoice with id: "
                                + invoice.getId(),
                        e
                );

            } finally {

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing PreparedStatement: "
                                        + e.getMessage()
                        );
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing Connection: "
                                        + e.getMessage()
                        );
                    }
                }
            }
        }

        @Override
        public void deleteById(Long id) {

            String sql = """
                DELETE FROM invoices
                WHERE id = ?
                """;

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = databaseConnection.getConnection();
                statement = connection.prepareStatement(sql);

                statement.setLong(1, id);

                int rowsAffected =
                        statement.executeUpdate();

                if (rowsAffected == 0) {

                    throw new RuntimeException(
                            "Invoice not found with id: " + id
                    );
                }

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Error deleting invoice with id: " + id,
                        e
                );

            } finally {

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing PreparedStatement: "
                                        + e.getMessage()
                        );
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(
                                "Error closing Connection: "
                                        + e.getMessage()
                        );
                    }
                }
            }
        }

        private Invoice mapResultSetToInvoice(
                ResultSet resultSet
        ) throws SQLException {

            Invoice invoice = new Invoice();

            invoice.setId(
                    resultSet.getLong("id")
            );

            invoice.setPaymentId(
                    resultSet.getLong("payment_id")
            );

            invoice.setInvoiceNumber(
                    resultSet.getString("invoice_number")
            );

            invoice.setSubtotalHt(
                    resultSet.getBigDecimal("subtotal_ht")
            );

            invoice.setTaxAmount(
                    resultSet.getBigDecimal("tax_amount")
            );

            invoice.setTotalTtc(
                    resultSet.getBigDecimal("total_ttc")
            );

            invoice.setIssuedAt(
                    resultSet.getObject(
                            "issued_at",
                            LocalDateTime.class
                    )
            );

            return invoice;
        }
    }
}