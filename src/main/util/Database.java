package main.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for managing MySQL database connections and operations.
 * This class provides a centralized way to handle database connectivity,
 * connection lifecycle, and basic query execution for the EMR system.
 *
 * The class uses environment variables for secure configuration:
 * - EMR_DB_URL: JDBC connection string
 * - EMR_DB_USER: Database username
 * - EMR_DB_PASSWORD: Database password
 */
public class Database {

    /** JDBC connection URL for the MySQL database */
    private final String URL;

    /** Database username for authentication */
    private final String USER;

    /** Database password for authentication */
    private final String PASSWORD;

    /** Active database connection instance */
    private Connection connection;

    /**
     * Constructs a new Database utility instance.
     * Initializes database connection parameters from environment variables
     * and establishes the connection immediately.
     *
     * Environment variables used:
     * - EMR_DB_URL (defaults to localhost if not set)
     * - EMR_DB_USER (defaults to placeholder if not set)
     * - EMR_DB_PASSWORD (defaults to placeholder if not set)
     *
     * @throws RuntimeException if database connection fails
     */
    public Database() {
        // Load database configuration from environment variables with fallbacks
        this.URL = System.getenv("EMR_DB_URL") != null ? System.getenv("EMR_DB_URL")
                : "jdbc:mysql://localhost:3306/your_database_name";
        this.USER = System.getenv("EMR_DB_USER") != null ? System.getenv("EMR_DB_USER") : "your_username";
        this.PASSWORD = System.getenv("EMR_DB_PASSWORD") != null ? System.getenv("EMR_DB_PASSWORD") : "your_password";

        // Establish database connection
        connect();
    }

    /**
     * Establishes a connection to the MySQL database.
     * Uses JDBC DriverManager to create a connection with the configured
     * parameters.
     *
     * @throws RuntimeException if connection fails (wraps SQLException)
     */
    private void connect() {
        try {
            // Create database connection using JDBC
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("=== Connected to MySQL database ===\n");
        } catch (SQLException e) {
            // Print error message and re-throw as RuntimeException for easier handling
            System.err.println(
                    "!!! Connection failed: " + e.getMessage() + " (" + e.getClass().getSimpleName() + ") !!!\n");
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the database connection and releases resources.
     * Should be called when the application is shutting down to ensure
     * proper cleanup of database resources.
     */
    public void close() {
        if (connection != null) {
            try {
                // Close the database connection
                connection.close();
                System.out.println("=== Database connection closed ===\n");
            } catch (SQLException e) {
                // Log error if connection closure fails
                System.err.println("!!! Error closing connection: " + e.getMessage() + " ("
                        + e.getClass().getSimpleName() + ") !!!\n");
            }
        }
    }

    /**
     * Returns the active database connection.
     * This connection can be used by DAO classes to execute SQL statements.
     *
     * @return the active Connection object
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Executes a SELECT query and returns the result set.
     * This is a convenience method for simple queries that don't require
     * parameters.
     *
     * @param sql the SQL SELECT statement to execute
     * @return ResultSet containing the query results
     * @throws SQLException if a database access error occurs or the SQL is invalid
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        return stmt.executeQuery();
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE statement.
     * This is a convenience method for simple DML operations that don't require
     * parameters.
     *
     * @param sql the SQL DML statement to execute
     * @return the number of rows affected by the statement
     * @throws SQLException if a database access error occurs or the SQL is invalid
     */
    public int executeUpdate(String sql) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return stmt.executeUpdate();
        }
    }
}
