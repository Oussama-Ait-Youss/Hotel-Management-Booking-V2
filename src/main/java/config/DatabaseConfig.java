package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final String CONFIG_FILE = "db.properties";

    private final Properties properties;

    public DatabaseConfig() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException(
                        "Configuration file not found: " + CONFIG_FILE
                );
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load database configuration",
                    e
            );
        }
    }

    public String getUrl() {
        return properties.getProperty("db.url");
    }

    public String getUsername() {
        return properties.getProperty("db.username");
    }

    public String getPassword() {
        return properties.getProperty("db.password");
    }
}