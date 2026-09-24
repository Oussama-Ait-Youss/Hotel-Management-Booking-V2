package repository.jdbc;

import db.DatabaseConnection;
import dto.AvailableRoomDTO;
import dto.RoomSearchCriteria;
import model.Room;
import model.enums.RoomStatus;
import model.enums.RoomType;
import repository.RoomRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcRoomRepository implements RoomRepository {

    private final DatabaseConnection databaseConnection;

    public JdbcRoomRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    @Override
    public Optional<Room> findById(Long id) {

        String sql = """
                SELECT id,
                       room_number,
                       type,
                       capacity,
                       base_price,
                       status,
                       description
                FROM rooms
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

                Room room = new Room();

                room.setId(resultSet.getLong("id"));
                room.setRoomNumber(resultSet.getString("room_number"));

                room.setType(
                        RoomType.valueOf(
                                resultSet.getString("type")
                        )
                );

                room.setCapacity(
                        resultSet.getInt("capacity")
                );

                room.setBasePrice(
                        resultSet.getBigDecimal("base_price")
                );

                room.setStatus(
                        RoomStatus.valueOf(
                                resultSet.getString("status")
                        )
                );

                room.setDescription(
                        resultSet.getString("description")
                );

                return Optional.of(room);
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding room with id: " + id,
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing ResultSet: " + e.getMessage()
                    );
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: " + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: " + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public Optional<Room> findByRoomNumber(String roomNumber) {

        String sql = """
                SELECT id,
                       room_number,
                       type,
                       capacity,
                       base_price,
                       status,
                       description
                FROM rooms
                WHERE room_number = ?
                """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setString(1, roomNumber);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Room room = new Room();

                room.setId(resultSet.getLong("id"));
                room.setRoomNumber(resultSet.getString("room_number"));

                room.setType(
                        RoomType.valueOf(
                                resultSet.getString("type")
                        )
                );

                room.setCapacity(
                        resultSet.getInt("capacity")
                );

                room.setBasePrice(
                        resultSet.getBigDecimal("base_price")
                );

                room.setStatus(
                        RoomStatus.valueOf(
                                resultSet.getString("status")
                        )
                );

                room.setDescription(
                        resultSet.getString("description")
                );

                return Optional.of(room);
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding room with number: " + roomNumber,
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing ResultSet: " + e.getMessage()
                    );
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: " + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: " + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public List<Room> findAll() {

        String sql = """
                SELECT id,
                       room_number,
                       type,
                       capacity,
                       base_price,
                       status,
                       description
                FROM rooms
                ORDER BY id
                """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Room> rooms = new ArrayList<>();

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Room room = new Room();

                room.setId(resultSet.getLong("id"));
                room.setRoomNumber(resultSet.getString("room_number"));

                room.setType(
                        RoomType.valueOf(
                                resultSet.getString("type")
                        )
                );

                room.setCapacity(
                        resultSet.getInt("capacity")
                );

                room.setBasePrice(
                        resultSet.getBigDecimal("base_price")
                );

                room.setStatus(
                        RoomStatus.valueOf(
                                resultSet.getString("status")
                        )
                );

                room.setDescription(
                        resultSet.getString("description")
                );

                rooms.add(room);
            }

            return rooms;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding all rooms",
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing ResultSet: " + e.getMessage()
                    );
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: " + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: " + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public List<AvailableRoomDTO> findAvailableRooms(RoomSearchCriteria criteria) {
        String sql = """
            SELECT id, room_number, type, capacity, base_price, description
            FROM rooms
            WHERE status = 'AVAILABLE'
            AND id NOT IN (
                SELECT room_id FROM reservations 
                WHERE status IN ('PENDING', 'CONFIRMED')
                AND check_in < ? AND check_out > ?
            )
            """;

        List<AvailableRoomDTO> availableRooms = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();
            statement = connection.prepareStatement(sql);

            // Si checkOut est avant le checkIn existant, pas de chevauchement. Donc on cherche les chevauchements.
            statement.setObject(1, criteria.getCheckOut());
            statement.setObject(2, criteria.getCheckIn());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                AvailableRoomDTO dto = new AvailableRoomDTO();
                dto.setRoomId(resultSet.getLong("id"));
                dto.setRoomNumber(resultSet.getString("room_number"));
                dto.setRoomType(RoomType.valueOf(resultSet.getString("type")));
                dto.setBasePrice(resultSet.getBigDecimal("base_price"));
                availableRooms.add(dto);
            }
            return availableRooms;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des chambres disponibles", e);
        } finally {
            // ... (gardez votre bloc finally habituel de fermeture de connexion)
            try { if(resultSet!=null) resultSet.close(); if(statement!=null) statement.close(); if(connection!=null) connection.close(); } catch(Exception e){}
        }
    }

    @Override
    public Room save(Room room) {

        String sql = """
                INSERT INTO rooms (
                    room_number,
                    type,
                    capacity,
                    base_price,
                    status,
                    description
                )
                VALUES (?, ?::room_type, ?, ?, ?::room_status, ?)
                RETURNING id
                """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setString(1, room.getRoomNumber());
            statement.setString(2, room.getType().name());
            statement.setInt(3, room.getCapacity());
            statement.setBigDecimal(4, room.getBasePrice());
            statement.setString(5, room.getStatus().name());
            statement.setString(6, room.getDescription());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                room.setId(
                        resultSet.getLong("id")
                );

                return room;
            }

            throw new RuntimeException("Failed to save room");

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error saving room",
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing ResultSet: " + e.getMessage()
                    );
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: " + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: " + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public void update(Room room) {

        String sql = """
                UPDATE rooms
                SET room_number = ?,
                    type = ?::room_type,
                    capacity = ?,
                    base_price = ?,
                    status = ?::room_status,
                    description = ?
                WHERE id = ?
                """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setString(1, room.getRoomNumber());
            statement.setString(2, room.getType().name());
            statement.setInt(3, room.getCapacity());
            statement.setBigDecimal(4, room.getBasePrice());
            statement.setString(5, room.getStatus().name());
            statement.setString(6, room.getDescription());
            statement.setLong(7, room.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException(
                        "Room not found with id: " + room.getId()
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating room with id: " + room.getId(),
                    e
            );

        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: " + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: " + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public void deleteById(Long id) {

        String sql = """
                DELETE FROM rooms
                WHERE id = ?
                """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException(
                        "Room not found with id: " + id
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error deleting room with id: " + id,
                    e
            );

        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing PreparedStatement: " + e.getMessage()
                    );
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing Connection: " + e.getMessage()
                    );
                }
            }
        }
    }
}