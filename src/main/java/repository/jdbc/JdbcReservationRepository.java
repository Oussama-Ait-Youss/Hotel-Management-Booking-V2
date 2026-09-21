package repository.jdbc;

import db.DatabaseConnection;
import dto.ReservationSummaryDTO;
import model.Reservation;
import model.enums.ReservationStatus;
import repository.ReservationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcReservationRepository implements ReservationRepository {

    private final DatabaseConnection databaseConnection;

    public JdbcReservationRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    @Override
    public Optional<Reservation> findById(Long id) {

        String sql = """
            SELECT id,
                   user_id,
                   room_id,
                   check_in,
                   check_out,
                   status,
                   total_amount,
                   created_at
            FROM reservations
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

                Reservation reservation = new Reservation();

                reservation.setId(
                        resultSet.getLong("id")
                );

                reservation.setUserId(
                        resultSet.getLong("user_id")
                );

                reservation.setRoomId(
                        resultSet.getLong("room_id")
                );

                reservation.setCheckIn(
                        resultSet.getObject(
                                "check_in",
                                LocalDate.class
                        )
                );

                reservation.setCheckOut(
                        resultSet.getObject(
                                "check_out",
                                LocalDate.class
                        )
                );

                reservation.setStatus(
                        ReservationStatus.valueOf(
                                resultSet.getString("status")
                        )
                );

                reservation.setTotalAmount(
                        resultSet.getBigDecimal("total_amount")
                );

                reservation.setCreatedAt(
                        resultSet.getObject(
                                "created_at",
                                LocalDateTime.class
                        )
                );

                return Optional.of(reservation);
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding reservation with id: " + id,
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
    public List<ReservationSummaryDTO> findByUserId(Long userId) {

        String sql = """
            SELECT r.id AS reservation_id,
                   rm.room_number,
                   r.check_in,
                   r.check_out,
                   r.status,
                   r.total_amount
            FROM reservations r
            JOIN rooms rm
                ON r.room_id = rm.id
            WHERE r.user_id = ?
            ORDER BY r.check_in DESC
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<ReservationSummaryDTO> reservations =
                new ArrayList<>();

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, userId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                ReservationSummaryDTO dto =
                        new ReservationSummaryDTO();

                dto.setReservationId(
                        resultSet.getLong("reservation_id")
                );

                dto.setRoomNumber(
                        resultSet.getString("room_number")
                );

                dto.setCheckIn(
                        resultSet.getObject(
                                "check_in",
                                LocalDate.class
                        )
                );

                dto.setCheckOut(
                        resultSet.getObject(
                                "check_out",
                                LocalDate.class
                        )
                );

                dto.setStatus(
                        ReservationStatus.valueOf(
                                resultSet.getString("status")
                        )
                );

                dto.setTotalAmount(
                        resultSet.getBigDecimal("total_amount")
                );

                reservations.add(dto);
            }

            return reservations;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding reservations for user: "
                            + userId,
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
    public List<Reservation> findByRoomId(Long roomId) {

        String sql = """
            SELECT id,
                   user_id,
                   room_id,
                   check_in,
                   check_out,
                   status,
                   total_amount,
                   created_at
            FROM reservations
            WHERE room_id = ?
            ORDER BY check_in
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Reservation> reservations =
                new ArrayList<>();

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, roomId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Reservation reservation =
                        mapResultSetToReservation(resultSet);

                reservations.add(reservation);
            }

            return reservations;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding reservations for room: "
                            + roomId,
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
    public List<Reservation> findByStatus(
            ReservationStatus status
    ) {

        String sql = """
            SELECT id,
                   user_id,
                   room_id,
                   check_in,
                   check_out,
                   status,
                   total_amount,
                   created_at
            FROM reservations
            WHERE status = ?::reservation_status
            ORDER BY check_in
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Reservation> reservations =
                new ArrayList<>();

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setString(1, status.name());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Reservation reservation =
                        mapResultSetToReservation(resultSet);

                reservations.add(reservation);
            }

            return reservations;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding reservations with status: "
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
    public boolean existsOverlappingReservation(
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {

        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM reservations
                WHERE room_id = ?
                  AND status IN ('PENDING', 'CONFIRMED')
                  AND check_in < ?
                  AND check_out > ?
            )
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, roomId);
            statement.setObject(2, checkOut);
            statement.setObject(3, checkIn);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }

            return false;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error checking reservation overlap",
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
    public Reservation save(Reservation reservation) {

        String sql = """
            INSERT INTO reservations (
                user_id,
                room_id,
                check_in,
                check_out,
                status,
                total_amount
            )
            VALUES (
                ?, ?, ?, ?, ?::reservation_status, ?
            )
            RETURNING id, created_at
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, reservation.getUserId());
            statement.setLong(2, reservation.getRoomId());
            statement.setObject(3, reservation.getCheckIn());
            statement.setObject(4, reservation.getCheckOut());
            statement.setString(5, reservation.getStatus().name());
            statement.setBigDecimal(
                    6,
                    reservation.getTotalAmount()
            );

            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                reservation.setId(
                        resultSet.getLong("id")
                );

                reservation.setCreatedAt(
                        resultSet.getObject(
                                "created_at",
                                LocalDateTime.class
                        )
                );

                return reservation;
            }

            throw new RuntimeException(
                    "Failed to save reservation"
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error saving reservation",
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
    public void update(Reservation reservation) {

        String sql = """
            UPDATE reservations
            SET user_id = ?,
                room_id = ?,
                check_in = ?,
                check_out = ?,
                status = ?::reservation_status,
                total_amount = ?
            WHERE id = ?
            """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {

            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, reservation.getUserId());
            statement.setLong(2, reservation.getRoomId());
            statement.setObject(3, reservation.getCheckIn());
            statement.setObject(4, reservation.getCheckOut());
            statement.setString(5, reservation.getStatus().name());
            statement.setBigDecimal(
                    6,
                    reservation.getTotalAmount()
            );
            statement.setLong(7, reservation.getId());

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {

                throw new RuntimeException(
                        "Reservation not found with id: "
                                + reservation.getId()
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating reservation with id: "
                            + reservation.getId(),
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
            DELETE FROM reservations
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
                        "Reservation not found with id: " + id
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error deleting reservation with id: " + id,
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

    private Reservation mapResultSetToReservation(
            ResultSet resultSet
    ) throws SQLException {

        Reservation reservation = new Reservation();

        reservation.setId(
                resultSet.getLong("id")
        );

        reservation.setUserId(
                resultSet.getLong("user_id")
        );

        reservation.setRoomId(
                resultSet.getLong("room_id")
        );

        reservation.setCheckIn(
                resultSet.getObject(
                        "check_in",
                        LocalDate.class
                )
        );

        reservation.setCheckOut(
                resultSet.getObject(
                        "check_out",
                        LocalDate.class
                )
        );

        reservation.setStatus(
                ReservationStatus.valueOf(
                        resultSet.getString("status")
                )
        );

        reservation.setTotalAmount(
                resultSet.getBigDecimal("total_amount")
        );

        reservation.setCreatedAt(
                resultSet.getObject(
                        "created_at",
                        LocalDateTime.class
                )
        );

        return reservation;
    }
}