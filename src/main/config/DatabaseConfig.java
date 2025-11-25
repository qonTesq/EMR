package main.config;

/**
 * Configuration class for database connection settings.
 * <p>
 * This class centralizes database configuration management and provides secure
 * configuration loading from environment variables with sensible defaults.
 * It follows the configuration pattern to separate configuration concerns
 * from business logic.
 * </p>
 *
 * <h3>Environment Variables:</h3>
 * <ul>
 * <li><b>EMR_DB_URL</b> - JDBC connection URL (default:
 * jdbc:mysql://localhost:3306/emr_db)</li>
 * <li><b>EMR_DB_USER</b> - Database username (default: root)</li>
 * <li><b>EMR_DB_PASSWORD</b> - Database password (default: empty string)</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 *
 * <pre>
 * // Using environment variables
 * DatabaseConfig config = new DatabaseConfig();
 *
 * // Using custom values
 * DatabaseConfig config = new DatabaseConfig(
 *         "jdbc:mysql://prod-server:3306/emr",
 *         "admin",
 *         "secure_password");
 * </pre>
 */
public class DatabaseConfig {

    /** Default JDBC connection URL pointing to local MySQL instance */
    private static final String DEFAULT_URL =
        "jdbc:mysql://localhost:3306/emr_db";

    /** Default database username for local development */
    private static final String DEFAULT_USER = "root";

    /** Default database password (empty for local development) */
    private static final String DEFAULT_PASSWORD = "";

    /** JDBC connection URL for the database */
    private final String url;

    /** Username for database authentication */
    private final String user;

    /** Password for database authentication */
    private final String password;

    /**
     * Creates database configuration from environment variables with fallback
     * defaults.
     * <p>
     * This constructor reads configuration from environment variables, falling back
     * to default values if environment variables are not set. This approach allows
     * for secure configuration in production while maintaining ease of use in
     * development.
     * </p>
     *
     * @see #DatabaseConfig(String, String, String) for custom configuration
     */
    public DatabaseConfig() {
        // Load configuration from environment with defaults as fallback
        this.url = getEnvOrDefault("EMR_DB_URL", DEFAULT_URL);
        this.user = getEnvOrDefault("EMR_DB_USER", DEFAULT_USER);
        this.password = getEnvOrDefault("EMR_DB_PASSWORD", DEFAULT_PASSWORD);
    }

    /**
     * Creates database configuration with specified values.
     * <p>
     * This constructor allows for explicit configuration, useful for testing
     * or when configuration comes from a different source than environment
     * variables.
     * </p>
     *
     * @param url      the JDBC connection URL (must not be null)
     * @param user     the database username (must not be null)
     * @param password the database password (may be empty but not null)
     */
    public DatabaseConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Retrieves an environment variable value or returns the default if not set.
     * <p>
     * This utility method safely reads environment variables, providing a fallback
     * value when the variable is not defined in the system environment.
     * </p>
     *
     * @param key          the environment variable name
     * @param defaultValue the fallback value if environment variable is not set
     * @return the environment variable value or default value
     */
    private String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Returns the JDBC connection URL.
     *
     * @return the database connection URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the database username.
     *
     * @return the database username for authentication
     */
    public String getUser() {
        return user;
    }

    /**
     * Returns the database password.
     *
     * @return the database password for authentication
     */
    public String getPassword() {
        return password;
    }
}
