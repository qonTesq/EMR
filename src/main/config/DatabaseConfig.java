package main.config;

public class DatabaseConfig {

    private static final String DEFAULT_URL =
        "jdbc:mysql://localhost:3306/emr_db";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private final String url;
    private final String user;
    private final String password;

    public DatabaseConfig() {
        this.url = getEnvOrDefault("EMR_DB_URL", DEFAULT_URL);
        this.user = getEnvOrDefault("EMR_DB_USER", DEFAULT_USER);
        this.password = getEnvOrDefault("EMR_DB_PASSWORD", DEFAULT_PASSWORD);
    }

    public DatabaseConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
