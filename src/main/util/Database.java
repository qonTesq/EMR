package main.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import main.config.DatabaseConfig;

/**
 * Utility class for managing MySQL database connections.
 * <p>
 * This class provides centralized database connectivity and connection
 * lifecycle
 * management for the EMR system. It uses the DatabaseConfig class for
 * configuration
 * and follows the facade pattern to simplify database connection management.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 * <li>Automatic connection establishment on instantiation</li>
 * <li>Configurable via DatabaseConfig object</li>
 * <li>Proper resource cleanup with close() method</li>
 * <li>Exception handling with meaningful error messages</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 *
 * <pre>
 * // Using default configuration
 * Database db = new Database();
 * try {
 *     Connection conn = db.getConnection();
 *     // Perform database operations
 * } finally {
 *     db.close();
 * }
 * </pre>
 *
 * @see DatabaseConfig
 */
public class Database {

    /** Configuration object containing database connection parameters */
    private final DatabaseConfig config;

    /** Active JDBC connection to the database */
    private Connection connection;

    /**
     * Constructs a Database instance with default configuration.
     * <p>
     * This constructor uses {@link DatabaseConfig} with default settings,
     * which reads from environment variables or uses built-in defaults.
     * The connection is established immediately upon construction.
     * </p>
     *
     * @throws RuntimeException if database connection fails, wrapping the
     *                          underlying SQLException
     * @see #Database(DatabaseConfig) for custom configuration
     */
    public Database() {
        this(new DatabaseConfig());
    }

    /**
     * Constructs a Database instance with custom configuration.
     * <p>
     * This constructor allows injection of a custom DatabaseConfig,
     * useful for testing or when using non-standard configurations.
     * The connection is established immediately upon construction.
     * </p>
     *
     * @param config the database configuration object containing connection
     *               parameters
     * @throws RuntimeException     if database connection fails, wrapping the
     *                              underlying SQLException
     * @throws NullPointerException if config is null
     */
    public Database(DatabaseConfig config) {
        this.config = config;
        connect();
    }

    /**
     * Establishes connection to the MySQL database.
     * <p>
     * This method is called automatically during construction. It uses JDBC's
     * DriverManager to establish a connection with the parameters from the
     * configuration object. On successful connection, a confirmation message
     * is printed to stdout. On failure, an error message is printed to stderr
     * and a RuntimeException is thrown.
     * </p>
     *
     * @throws RuntimeException if connection fails, wrapping the underlying
     *                          SQLException
     */
    private void connect() {
        try {
            // Establish JDBC connection using configuration parameters
            connection = DriverManager.getConnection(
                config.getUrl(),
                config.getUser(),
                config.getPassword()
            );
            // Confirm successful connection
            System.out.println("=== Connected to MySQL database ===\n");
        } catch (SQLException e) {
            // Log error and re-throw as unchecked exception for easier handling
            System.err.println(
                "!!! Connection failed: " + e.getMessage() + " !!!\n"
            );
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    /**
     * Closes the database connection and releases all associated resources.
     * <p>
     * This method should always be called when the Database object is no longer
     * needed,
     * typically in a finally block or using try-with-resources pattern. It safely
     * closes
     * the connection even if it's null, and handles any exceptions that occur
     * during closure.
     * Calling this method multiple times is safe - subsequent calls have no effect.
     * </p>
     *
     * <h3>Best Practice:</h3>
     *
     * <pre>
     * Database db = new Database();
     * try {
     *     // Use database
     * } finally {
     *     db.close(); // Always close in finally block
     * }
     * </pre>
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("=== Database connection closed ===\n");
            } catch (SQLException e) {
                // Log error but don't throw - cleanup should not fail the application
                System.err.println(
                    "!!! Error closing connection: " + e.getMessage() + " !!!\n"
                );
            }
        }
    }

    /**
     * Returns the active database connection.
     * <p>
     * This method provides access to the underlying JDBC Connection object,
     * which can be used by DAO classes to create PreparedStatements and
     * execute SQL queries.
     * </p>
     *
     * @return the active JDBC Connection object, or null if connection was closed
     */
    public Connection getConnection() {
        return connection;
    }
}
