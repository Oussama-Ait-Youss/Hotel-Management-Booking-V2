package repository.jdbc;

import db.DatabaseConnection;
import model.Refund;
import model.enums.RefundStatus;
import repository.RefundRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcRefundRepository implements RefundRepository {

    private final DatabaseConnection databaseConnection;

    public JdbcRefundRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    @Override
    public Optional<Refund> findById(Long id) {

        String sql = """
                SELECT id,
                       payment_id,
                       amount,
                       status,
                       reason,
                       created_at,
                       processed_at
                FROM refunds
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
                return Optional.of(mapResultSetToRefund(resultSet));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding refund by ID: " + id,
                    e
            );

        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Refund> findByPaymentId(Long paymentId) {

        String sql = """
                SELECT id,
                       payment_id,
                       amount,
                       status,
                       reason,
                       created_at,
                       processed_at
                FROM refunds
                WHERE payment_id = ?
                ORDER BY created_at DESC
                """;

        List<Refund> refunds = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, paymentId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                refunds.add(mapResultSetToRefund(resultSet));
            }

            return refunds;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding refunds for payment ID: " + paymentId,
                    e
            );

        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Refund> findByStatus(RefundStatus status) {

        String sql = """
                SELECT id,
                       payment_id,
                       amount,
                       status,
                       reason,
                       created_at,
                       processed_at
                FROM refunds
                WHERE status = ?::refund_status
                ORDER BY created_at DESC
                """;

        List<Refund> refunds = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, status.name());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                refunds.add(mapResultSetToRefund(resultSet));
            }

            return refunds;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding refunds by status: " + status,
                    e
            );

        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Refund> findAll() {

        String sql = """
                SELECT id,
                       payment_id,
                       amount,
                       status,
                       reason,
                       created_at,
                       processed_at
                FROM refunds
                ORDER BY created_at DESC
                """;

        List<Refund> refunds = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                refunds.add(mapResultSetToRefund(resultSet));
            }

            return refunds;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding all refunds",
                    e
            );

        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Refund save(Refund refund) {

        String sql = """
                INSERT INTO refunds (
                    payment_id,
                    amount,
                    status,
                    reason,
                    processed_at
                )
                VALUES (
                    ?,
                    ?,
                    ?::refund_status,
                    ?,
                    ?
                )
                RETURNING id, created_at
                """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, refund.getPaymentId());
            statement.setBigDecimal(2, refund.getAmount());
            statement.setString(3, refund.getStatus().name());
            statement.setString(4, refund.getReason());
            statement.setObject(5, refund.getProcessedAt());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                refund.setId(resultSet.getLong("id"));
                refund.setCreatedAt(
                        resultSet.getObject(
                                "created_at",
                                LocalDateTime.class
                        )
                );
            }

            return refund;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error saving refund",
                    e
            );

        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public void update(Refund refund) {

        String sql = """
                UPDATE refunds
                SET payment_id = ?,
                    amount = ?,
                    status = ?::refund_status,
                    reason = ?,
                    processed_at = ?
                WHERE id = ?
                """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, refund.getPaymentId());
            statement.setBigDecimal(2, refund.getAmount());
            statement.setString(3, refund.getStatus().name());
            statement.setString(4, refund.getReason());
            statement.setObject(5, refund.getProcessedAt());
            statement.setLong(6, refund.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating refund with ID: " + refund.getId(),
                    e
            );

        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public void deleteById(Long id) {

        String sql = """
                DELETE FROM refunds
                WHERE id = ?
                """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error deleting refund with ID: " + id,
                    e
            );

        } finally {
            closeResources(null, statement, connection);
        }
    }

    private Refund mapResultSetToRefund(ResultSet resultSet)
            throws SQLException {

        Refund refund = new Refund();

        refund.setId(resultSet.getLong("id"));
        refund.setPaymentId(resultSet.getLong("payment_id"));

        refund.setAmount(
                resultSet.getBigDecimal("amount")
        );

        refund.setStatus(
                RefundStatus.valueOf(
                        resultSet.getString("status")
                )
        );

        refund.setReason(
                resultSet.getString("reason")
        );

        refund.setCreatedAt(
                resultSet.getObject(
                        "created_at",
                        LocalDateTime.class
                )
        );

        refund.setProcessedAt(
                resultSet.getObject(
                        "processed_at",
                        LocalDateTime.class
                )
        );

        return refund;
    }

    private void closeResources(
            ResultSet resultSet,
            PreparedStatement statement,
            Connection connection
    ) {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}