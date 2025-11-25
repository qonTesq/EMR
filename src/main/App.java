package main;

import main.cli.MainCLI;
import main.util.Database;

/**
 * Main application class for the Electronic Medical Records (EMR) system.
 * <p>
 * This class serves as the entry point for the CLI-based EMR application.
 * It initializes the database connection and starts the main user interface,
 * ensuring proper resource cleanup even in case of errors.
 * </p>
 * 
 * <h3>Application Flow:</h3>
 * <ol>
 * <li>Initialize database connection</li>
 * <li>Launch main CLI interface</li>
 * <li>Handle user interactions until exit</li>
 * <li>Cleanup and close database connection</li>
 * </ol>
 * 
 * <h3>Configuration:</h3>
 * <p>
 * The application reads database configuration from environment variables:
 * </p>
 * <ul>
 * <li><b>EMR_DB_URL</b> - Database connection URL</li>
 * <li><b>EMR_DB_USER</b> - Database username</li>
 * <li><b>EMR_DB_PASSWORD</b> - Database password</li>
 * </ul>
 *
 * @see MainCLI
 * @see Database
 */
public class App {

    /**
     * Main entry point for the EMR application.
     * <p>
     * This method initializes the database, starts the CLI interface, and ensures
     * proper cleanup of resources. The application follows a try-finally pattern
     * to guarantee database closure even if errors occur during execution.
     * </p>
     * 
     * <h3>Error Handling:</h3>
     * <ul>
     * <li>Database connection failures cause immediate exit with error code 1</li>
     * <li>Runtime exceptions are caught and logged before exiting</li>
     * <li>Database connection is always closed in the finally block</li>
     * </ul>
     * 
     * @param args command line arguments (currently unused)
     */
    public static void main(String[] args) {
        Database db = null;

        try {
            // Initialize database connection (reads from environment variables)
            db = new Database();

            // Start the main user interface loop
            new MainCLI(db).start();

        } catch (RuntimeException e) {
            // Handle fatal errors (e.g., database connection failure)
            System.err.println("Fatal error: " + e.getMessage());
            System.exit(1);

        } finally {
            // Always cleanup database resources, even if errors occurred
            if (db != null) {
                db.close();
            }
        }
    }
}