package main.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import main.config.DatabaseConfig;

public class Database {

    private final DatabaseConfig config;

    private Connection connection;

    public Database() {
        this(new DatabaseConfig());
    }

    public Database(DatabaseConfig config) {
        this.config = config;
        connect();
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(
                config.getUrl(),
                config.getUser(),
                config.getPassword()
            );

            System.out.println("=== Connected to MySQL database ===\n");
        } catch (SQLException e) {
            System.err.println(
                "!!! Connection failed: " + e.getMessage() + " !!!\n"
            );
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("=== Database connection closed ===\n");
            } catch (SQLException e) {
                System.err.println(
                    "!!! Error closing connection: " + e.getMessage() + " !!!\n"
                );
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
