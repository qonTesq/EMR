package main.cli;

import main.util.Database;

/**
 * Main Command Line Interface for the Electronic Medical Records (EMR)
 * Management System.
 * <p>
 * This class serves as the primary entry point for user interaction in the EMR
 * application.
 * It provides a hierarchical menu system that allows users to navigate to
 * different
 * management modules for Patients, Patient History, and Medical Procedures.
 * </p>
 * 
 * <h3>Available Modules:</h3>
 * <ul>
 * <li><b>Patients</b> - Manage patient demographic and insurance
 * information</li>
 * <li><b>Patient History</b> - Track medical procedures performed on
 * patients</li>
 * <li><b>Procedures</b> - Maintain catalog of available medical procedures</li>
 * </ul>
 * 
 * <h3>Navigation Flow:</h3>
 * 
 * <pre>
 * Main Menu
 *  ├─ Patients Management (PatientsCLI)
 *  ├─ Patient History Management (PatientHistoryCLI)
 *  ├─ Procedures Management (ProceduresCLI)
 *  └─ Exit
 * </pre>
 *
 * @see PatientsCLI
 * @see PatientHistoryCLI
 * @see ProceduresCLI
 */
public class MainCLI extends CLI {

    /** Welcome message displayed when the application starts */
    private static final String WELCOME_MESSAGE = "=== Welcome to EMR Management System ===\n";

    /** Goodbye message displayed when user exits the application */
    private static final String GOODBYE_MESSAGE = "\nThank you for using EMR Management System. Goodbye!\n";

    /** Database instance for passing to sub-modules */
    private final Database db;

    /**
     * Constructs a new MainCLI with the specified database connection.
     * <p>
     * The database connection is stored and passed to sub-modules as needed.
     * </p>
     * 
     * @param db the Database instance for database operations
     * @throws NullPointerException if db is null
     */
    public MainCLI(Database db) {
        super();
        this.db = db;
    }

    /**
     * Starts the main CLI interface and enters the primary interaction loop.
     * <p>
     * This method displays a welcome message and presents the main menu in a loop
     * until the user chooses to exit. It handles user navigation to different
     * management modules and manages the application's main execution flow.
     * </p>
     * 
     * <h3>Menu Options:</h3>
     * <ol>
     * <li>Patients - Opens patient management interface</li>
     * <li>Patient History - Opens patient history management interface</li>
     * <li>Procedures - Opens procedures management interface</li>
     * <li>Exit - Closes the application</li>
     * </ol>
     * 
     * @see PatientsCLI#start()
     * @see PatientHistoryCLI#start()
     * @see ProceduresCLI#start()
     */
    @Override
    public void start() {
        // Display welcome message to user
        System.out.println(WELCOME_MESSAGE);

        // Main application loop
        boolean running = true;
        while (running) {
            // Display menu and get user's choice
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");

            // Process user's menu selection
            switch (choice) {
                case 1:
                    // Navigate to Patients management
                    new PatientsCLI(db).start();
                    break;
                case 2:
                    // Navigate to Patient History management
                    new PatientHistoryCLI(db).start();
                    break;
                case 3:
                    // Navigate to Procedures management
                    new ProceduresCLI(db).start();
                    break;
                case 4:
                    // Exit application
                    running = false;
                    System.out.println(GOODBYE_MESSAGE);
                    break;
                default:
                    // Invalid choice - inform user
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the main menu options to the user.
     * <p>
     * This method prints a formatted menu showing all available management modules
     * and the exit option. Called once per iteration of the main loop.
     * </p>
     */
    private void showMainMenu() {
        System.out.println("\n=== EMR Management System ===");
        System.out.println("1. Patients");
        System.out.println("2. Patient History");
        System.out.println("3. Procedures");
        System.out.println("4. Exit");
        System.out.println("=============================");
    }
}