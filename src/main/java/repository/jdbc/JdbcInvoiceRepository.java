package repository.jdbc;

import db.DatabaseConnection;
import model.Invoice;
import repository.InvoiceRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcInvoiceRepository implements InvoiceRepository {

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