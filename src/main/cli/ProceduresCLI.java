package main.cli;

import java.sql.SQLException;
import main.dao.ProceduresDAO;
import main.models.Procedures;
import main.util.Database;

/**
 * Command Line Interface for managing medical procedures in the EMR system.
 * <p>
 * This class provides an interface for managing the catalog of available
 * medical
 * procedures. Procedures represent the various medical services, tests, and
 * treatments
 * that can be performed on patients. Each procedure has a unique identifier and
 * descriptive name. These procedures are referenced when creating patient
 * history records.
 * </p>
 *
 * <h3>Features:</h3>
 * <ul>
 * <li>Create new procedure definitions</li>
 * <li>Search and retrieve procedures by ID</li>
 * <li>View all available procedures</li>
 * <li>Update procedure information</li>
 * <li>Delete procedure definitions</li>
 * </ul>
 *
 * <h3>Example Procedures:</h3>
 * <ul>
 * <li>LAB-001: Complete Blood Count</li>
 * <li>IMG-005: MRI Scan Brain</li>
 * <li>SURG-012: Appendectomy</li>
 * </ul>
 *
 * @see ProceduresDAO
 * @see Procedures
 */
public class ProceduresCLI extends CLI {

    /**
     * Data Access Object for performing database operations on procedure records
     */
    private final ProceduresDAO proceduresDAO;

    /**
     * Constructs a new ProceduresCLI with the specified database connection.
     * <p>
     * Initializes the procedures data access object for database operations.
     * </p>
     *
     * @param db the Database instance for database operations
     * @throws NullPointerException if db is null
     */
    public ProceduresCLI(Database db) {
        super();
        this.proceduresDAO = new ProceduresDAO(db);
    }

    /**
     * Starts the procedures management interface and enters the interaction loop.
     * <p>
     * This method displays the procedures management menu and processes user
     * choices
     * until the user chooses to return to the main menu. Procedures defined here
     * become available for use when recording patient history.
     * </p>
     *
     * <h3>Menu Options:</h3>
     * <ol>
     * <li>Create Procedure - Add a new procedure to the catalog</li>
     * <li>Read Procedure by ID - Look up a specific procedure</li>
     * <li>Read All Procedures - View the complete procedure catalog</li>
     * <li>Update Procedure - Modify existing procedure information</li>
     * <li>Delete Procedure - Remove a procedure from the catalog</li>
     * <li>Back to Main Menu - Return to main application menu</li>
     * </ol>
     */
    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createProcedure();
                    break;
                case 2:
                    readProcedure();
                    break;
                case 3:
                    readAllProcedures();
                    break;
                case 4:
                    updateProcedure();
                    break;
                case 5:
                    deleteProcedure();
                    break;
                case 6:
                    running = false;
                    System.out.println("\nReturning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the procedures management menu options.
     * Shows all available operations for medical procedures management.
     */
    private void showMenu() {
        System.out.println("\n=== Procedures Management ===");
        System.out.println("1. Create Procedure");
        System.out.println("2. Read Procedure by ID");
        System.out.println("3. Read All Procedures");
        System.out.println("4. Update Procedure");
        System.out.println("5. Delete Procedure");
        System.out.println("6. Back to Main Menu");
        System.out.println("============================");
    }

    /**
     * Handles the creation of a new medical procedure definition in the system
     * catalog.
     * <p>
     * This method adds a new procedure type to the available procedures catalog.
     * Procedures defined here can be referenced when creating patient history
     * records.
     * Each procedure is identified by a unique ID and has a descriptive name.
     * </p>
     *
     * <h3>Required Information:</h3>
     * <ul>
     * <li><b>Procedure ID</b> - Unique identifier (e.g., "P001", "XRAY-001")</li>
     * <li><b>Procedure Name</b> - Descriptive name (e.g., "Blood Test", "X-Ray
     * Chest")</li>
     * </ul>
     *
     * <h3>Examples:</h3>
     * <ul>
     * <li>ID: "LAB-001", Name: "Complete Blood Count"</li>
     * <li>ID: "IMG-005", Name: "MRI Scan Brain"</li>
     * <li>ID: "SURG-012", Name: "Appendectomy"</li>
     * </ul>
     *
     * @see ProceduresDAO#createProcedure(Procedures)
     */
    private void createProcedure() {
        System.out.println("\n--- Create New Procedure ---");

        // Collect procedure information with validation
        String id = getRequiredStringInput("Enter Procedure ID: ");
        String name = getRequiredStringInput("Enter Procedure Name: ");
        String description = getRequiredStringInput(
            "Enter Procedure Description: "
        );
        int duration = getIntInput("Enter Procedure Duration (minutes): ");
        String doctorId = getRequiredStringInput("Enter Doctor ID: ");

        // Create procedure object with collected data
        Procedures procedure = new Procedures(
            id,
            name,
            description,
            duration,
            doctorId
        );

        // Attempt to save procedure to database
        try {
            if (proceduresDAO.createProcedure(procedure)) {
                System.out.println("Procedure created successfully!");
            } else {
                System.out.println("Failed to create procedure.");
            }
        } catch (Exception e) {
            // Handle database errors (e.g., duplicate procedure ID)
            System.out.println("Error creating procedure: " + e.getMessage());
        }
    }

    private void readProcedure() {
        System.out.println("\n--- Read Procedure (WIP) ---");
    }

    private void readAllProcedures() {
        System.out.println("\n--- All Procedures (WIP) ---");
    }

    private void updateProcedure() {
        System.out.println("\n--- Update Procedure ---");

        String id = getRequiredStringInput("Enter Procedure ID: ");
        Procedures procedures;
        try {
            procedures = proceduresDAO.getProcedure(id);
        } catch (SQLException e) {
            System.out.println("Error fetching procedure: " + e.getMessage());
            return;
        }

        if (procedures == null) {
            System.out.println("Procedure not found");
            return;
        }

        String input = getStringInput(
            "Update procedure name (leave empty to skip): "
        );
        if (!input.isEmpty()) {
            procedures.setName(input);
        }

        input = getStringInput(
            "Update procedure description (leave empty to skip): "
        );
        if (!input.isEmpty()) {
            procedures.setDescription(input);
        }

        input = getStringInput(
            "Update procedure duration (leave empty to skip): "
        );
        if (!input.isEmpty()) {
            try {
                procedures.setDuration(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid duration. Please enter a valid number."
                );
                return;
            }
        }

        input = getStringInput(
            "Update procedure Doctor ID (leave empty to skip): "
        );
        if (!input.isEmpty()) {
            procedures.setDoctorId(input);
        }

        try {
            boolean update = proceduresDAO.updateProcedure(procedures);
            if (update) {
                System.out.println("Procedure information has been updated");
            } else {
                System.out.println("Update failed");
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    private void deleteProcedure() {
        System.out.println("\n--- Delete Procedure (WIP) ---");
    }
}
