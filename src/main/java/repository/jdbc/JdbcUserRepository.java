package repository.jdbc;

import db.DatabaseConnection;
import model.User;
import model.enums.UserRole;
import org.w3c.dom.CDATASection;
import repository.UserRepository;

import javax.swing.text.html.Option;
import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final DatabaseConnection databaseConnection;

    public JdbcUserRepository(){
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    @Override
    public Optional<User> findById(Long id) {

        String sql = """
            SELECT id,
                   first_name,
                   last_name,
                   email,
                   password_hash,
                   salt,
                   role,
                   created_at
            FROM users
            WHERE id = ?
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            // IMPORTANT: no sql argument here
            resultSet = statement.executeQuery();

            // Did PostgreSQL return a row?
            if (resultSet.next()) {

                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setSalt(resultSet.getString("salt"));

                user.setRole(
                        UserRole.valueOf(
                                resultSet.getString("role")
                        )
                );

                user.setCreatedAt(
                        resultSet.getObject(
                                "created_at",
                                java.time.LocalDateTime.class
                        )
                );

                return Optional.of(user);
            }

            // No user found
            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding user with id: " + id,
                    e
            );

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Error closing ResultSet: " + e.getMessage());
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing Connection: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {

        String sql = """
            SELECT id,
                   first_name,
                   last_name,
                   email,
                   password_hash,
                   salt,
                   role,
                   created_at
            FROM users
            WHERE email = ?
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setString(1, email);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setSalt(resultSet.getString("salt"));

                user.setRole(
                        model.enums.UserRole.valueOf(
                                resultSet.getString("role")
                        )
                );

                user.setCreatedAt(
                        resultSet.getObject(
                                "created_at",
                                java.time.LocalDateTime.class
                        )
                );

                return Optional.of(user);
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding user with email: " + email,
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
    public List<User> findAll() {

        String sql = """
            SELECT id,
                   first_name,
                   last_name,
                   email,
                   password_hash,
                   salt,
                   role,
                   created_at
            FROM users
            ORDER BY id
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<User> users = new ArrayList<>();

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setSalt(resultSet.getString("salt"));

                user.setRole(
                        UserRole.valueOf(
                                resultSet.getString("role")
                        )
                );

                user.setCreatedAt(
                        resultSet.getObject(
                                "created_at",
                                LocalDateTime.class
                        )
                );

                users.add(user);
            }

            return users;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding all users",
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
    public User save(User user) {

        String sql = """
            INSERT INTO users (
                first_name,
                last_name,
                email,
                password_hash,
                salt,
                role
            )
            VALUES (?, ?, ?, ?, ?, ?::user_role)
            RETURNING id, created_at
            """;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPasswordHash());
            statement.setString(5, user.getSalt());
            statement.setString(6, user.getRole().name());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                user.setId(
                        resultSet.getLong("id")
                );

                user.setCreatedAt(
                        resultSet.getObject(
                                "created_at",
                                LocalDateTime.class
                        )
                );

                return user;
            }

            throw new RuntimeException("Failed to save user");

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error saving user",
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
    public void update(User user) {

        String sql = """
            UPDATE users
            SET first_name = ?,
                last_name = ?,
                email = ?,
                password_hash = ?,
                salt = ?,
                role = ?::user_role
            WHERE id = ?
            """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = databaseConnection.getConnection();

            statement = connection.prepareStatement(sql);

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPasswordHash());
            statement.setString(5, user.getSalt());
            statement.setString(6, user.getRole().name());

            statement.setLong(7, user.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException(
                        "User not found with id: " + user.getId()
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating user with id: " + user.getId(),
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
            DELETE FROM users
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
                        "User not found with id: " + id
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error deleting user with id: " + id,
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

//    public Optional<User> findById(Long id){
//        //where is the connection?
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//
//        try {
//            connection = databaseConnection.getConnection();
//            String sql = "SELECT * FROM users WHERE id = ?";
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setLong(1,id);
//            ResultSet result = preparedStatement.executeQuery();
//
//            if (result.next()) {
//                Long Userid  = result.getLong("id");
//                String first_name = result.getString("first_name");
//                String last_name = result.getString("last_name");
//                String email = result.getString("email");
//                String password_hash = result.getString("password_hash");
//                String salt = result.getString("salt");
//                UserRole role = UserRole.valueOf(result.getString("role"));
//                LocalDateTime created_at = result.getTimestamp("created_at").toLocalDateTime();
//
//                User user = new User(Userid,first_name,last_name,email,password_hash,salt,role,created_at);
//                return Optional.of(user);
//            }
//            return Optional.empty();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        //where is the sql query find user where id = id?
//        //where is the prepareStatment?
//        //where is the execution of the query?
//        //where is the exception handlling?
//    }
//    public Optional<User> findByEmail(String email){
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        String sql = "SELECT * FROM users WHERE email = ?";
//        try {
//            connection = databaseConnection.getConnection();
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1,email);
//            ResultSet result = preparedStatement.executeQuery();
//            if (result.next()) {
//                Long Userid  = result.getLong("id");
//                String first_name = result.getString("first_name");
//                String last_name = result.getString("last_name");
//                String Email = result.getString("email");
//                String password_hash = result.getString("password_hash");
//                String salt = result.getString("salt");
//                UserRole role = UserRole.valueOf(result.getString("role"));
//                LocalDateTime created_at = result.getTimestamp("created_at").toLocalDateTime();
//
//                User user = new User(Userid,first_name,last_name,Email,password_hash,salt,role,created_at);
//                return Optional.of(user);
//            }
//            return Optional.empty();
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//    @Override
//    public List<User> findAll() {
//
//        List<User> users = new ArrayList<>();
//
//        Connection connection = null;
//
//        try {
//            connection = databaseConnection.getConnection();
//
//            String sql = "SELECT * FROM users";
//
//            Statement stmt = connection.createStatement();
//
//            ResultSet result = stmt.executeQuery(sql);
//
//            while (result.next()) {
//
//                Long userId = result.getLong("id");
//                String firstName = result.getString("first_name");
//                String lastName = result.getString("last_name");
//                String email = result.getString("email");
//                String passwordHash = result.getString("password_hash");
//                String salt = result.getString("salt");
//
//                UserRole role =
//                        UserRole.valueOf(result.getString("role"));
//
//                LocalDateTime createdAt =
//                        result.getTimestamp("created_at")
//                                .toLocalDateTime();
//
//                User user = new User(
//                        userId,
//                        firstName,
//                        lastName,
//                        email,
//                        passwordHash,
//                        salt,
//                        role,
//                        createdAt
//                );
//
//                users.add(user);
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//
//        } finally {
//            try {
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return users;
//    }

}
