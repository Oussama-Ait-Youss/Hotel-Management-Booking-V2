package db;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static volatile DatabaseConnection instance;

    private final DatabaseConfig config;

    private DatabaseConnection() {
        this.config = new DatabaseConfig();
    }

    public static DatabaseConnection getInstance() {

        // First check: avoid synchronization if instance already exists
        if (instance == null) {

            synchronized (DatabaseConnection.class) {

                // Second check: another thread may have created it
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }

        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword()
        );
    }
}