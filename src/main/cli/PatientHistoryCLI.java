package main.cli;

import java.sql.SQLException;
import java.time.LocalDate;
import main.dao.PatientHistoryDAO;
import main.models.PatientHistory;
import main.util.Database;

/**
 * Command Line Interface for managing patient history records in the EMR
 * system.
 * <p>
 * This class provides a comprehensive interface for managing the relationship
 * between
 * patients and the medical procedures they receive. Patient history records
 * serve as
 * the core of the medical records system, tracking what procedures were
 * performed,
 * when they occurred, who performed them, and associated billing information.
 * </p>
 *
 * <h3>Features:</h3>
 * <ul>
 * <li>Create patient history records linking patients to procedures</li>
 * <li>Search and retrieve patient history by record ID</li>
 * <li>View complete patient history</li>
 * <li>Update existing history records</li>
 * <li>Delete history records</li>
 * </ul>
 *
 * @see PatientHistoryDAO
 * @see PatientHistory
 */
public class PatientHistoryCLI extends CLI {

    /**
     * Data Access Object for performing database operations on patient history
     * records
     */
    private final PatientHistoryDAO patientHistoryDAO;

    /**
     * Constructs a new PatientHistoryCLI with the specified database connection.
     * <p>
     * Initializes the patient history data access object for database operations.
     * </p>
     *
     * @param db the Database instance for database operations
     * @throws NullPointerException if db is null
     */
    public PatientHistoryCLI(Database db) {
        super();
        this.patientHistoryDAO = new PatientHistoryDAO(db);
    }

    /**
     * Starts the patient history management interface and enters the interaction
     * loop.
     * <p>
     * This method displays the patient history management menu and processes user
     * choices until the user chooses to return to the main menu. Patient history
     * operations require valid patient IDs and procedure IDs to maintain
     * referential
     * integrity.
     * </p>
     *
     * <h3>Menu Options:</h3>
     * <ol>
     * <li>Create Patient History - Record a new procedure performed on a
     * patient</li>
     * <li>Read Patient History by ID - Look up a specific history record</li>
     * <li>Read All Patient History - View all medical history records</li>
     * <li>Update Patient History - Modify existing history information</li>
     * <li>Delete Patient History - Remove a history record</li>
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
                    createPatientHistory();
                    break;
                case 2:
                    readPatientHistory();
                    break;
                case 3:
                    readAllPatientHistory();
                    break;
                case 4:
                    updatePatientHistory();
                    break;
                case 5:
                    deletePatientHistory();
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
     * Displays the patient history management menu options.
     * Shows all available operations for patient history management.
     */
    private void showMenu() {
        System.out.println("\n=== Patient History Management ===");
        System.out.println("1. Create Patient History");
        System.out.println("2. Read Patient History by ID");
        System.out.println("3. Read All Patient History");
        System.out.println("4. Update Patient History");
        System.out.println("5. Delete Patient History");
        System.out.println("6. Back to Main Menu");
        System.out.println("==================================");
    }

    /**
     * Handles the creation of a new patient history record linking a patient to a
     * procedure.
     * <p>
     * This method collects information about a medical procedure performed on a
     * patient,
     * including the procedure details, billing information, and attending
     * physician.
     * Patient history records serve as the junction between patients and the
     * procedures
     * they receive, maintaining a complete medical history with financial tracking.
     * </p>
     *
     * <h3>Required Information:</h3>
     * <ul>
     * <li><b>History ID</b> - Unique identifier for this history record</li>
     * <li><b>Patient ID</b> - MRN of the patient (must exist in patients
     * table)</li>
     * <li><b>Procedure ID</b> - ID of the procedure performed (must exist in
     * procedures table)</li>
     * <li><b>Date</b> - Date when the procedure was performed (yyyy-MM-dd
     * format)</li>
     * <li><b>Billing Amount</b> - Cost of the procedure in dollars</li>
     * <li><b>Doctor Name</b> - Name of the physician who performed the
     * procedure</li>
     * </ul>
     *
     * @see PatientHistoryDAO#createPatientHistory(PatientHistory)
     */
    private void createPatientHistory() {
        System.out.println("\n--- Create New Patient History ---");

        // Collect all required patient history information with validation
        String id = getRequiredStringInput("Enter History ID: ");
        int patientId = getIntInput("Enter Patient ID: ");
        String procedureId = getRequiredStringInput("Enter Procedure ID: ");
        LocalDate date = getDateInput("Enter Date (yyyy-MM-dd): ");
        double billing = getDoubleInput("Enter Billing Amount: ");
        String doctorId = getRequiredStringInput("Enter Doctor ID: ");

        // Create patient history object with collected data
        PatientHistory patientHistory = new PatientHistory(
            id,
            patientId,
            procedureId,
            date,
            billing,
            doctorId
        );

        // Attempt to save patient history record to database
        try {
            if (patientHistoryDAO.createPatientHistory(patientHistory)) {
                System.out.println("Patient history created successfully!");
            } else {
                System.out.println("Failed to create patient history.");
            }
        } catch (Exception e) {
            // Handle database errors (e.g., foreign key violations, duplicate IDs)
            System.out.println(
                "Error creating patient history: " + e.getMessage()
            );
        }
    }

    private void readPatientHistory() {
        System.out.println("\n--- Read Patient History (WIP) ---");
    }

    private void readAllPatientHistory() {
        System.out.println("\n--- All Patient History (WIP) ---");
    }

    private void updatePatientHistory() {
        System.out.println("\n--- Update Patient History ---");

        String patientHistoryID = getRequiredStringInput("Enter History ID: ");
        PatientHistory history;
        try {
            history = patientHistoryDAO.getPatientHistory(patientHistoryID);
        } catch (SQLException e) {
            System.out.println("Error retrieving record: " + e.getMessage());
            return;
        }

        if (history == null) {
            System.out.println("Record not found");
            return;
        }

        String input;

        input = getStringInput("Update Patient ID (leave empty to skip): ");
        if (!input.isEmpty()) {
            try {
                history.setPatientId(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid Patient ID. Please enter a valid number."
                );
                return;
            }
        }

        input = getStringInput("Update Procedure ID (leave empty to skip): ");
        if (!input.isEmpty()) {
            history.setProcedureId(input);
        }

        input = getStringInput(
            "Update Date of Procedure (yyyy-MM-dd, leave empty to skip): "
        );
        if (!input.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                history.setDate(date);
            } catch (Exception e) {
                System.out.println(
                    "Invalid date format. Please use yyyy-MM-dd format."
                );
                return;
            }
        }

        input = getStringInput("Update Billing Amount (leave empty to skip): ");
        if (!input.isEmpty()) {
            try {
                history.setBilling(Double.parseDouble(input));
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid billing amount. Please enter a valid number."
                );
                return;
            }
        }

        input = getStringInput("Update Doctor ID (leave empty to skip): ");
        if (!input.isEmpty()) {
            history.setDoctorId(input);
        }

        try {
            boolean update = patientHistoryDAO.updatePatientHistory(history);
            if (update) {
                System.out.println("Patient history has been updated");
            } else {
                System.out.println("Update failed");
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    private void deletePatientHistory() {
        System.out.println("\n--- Delete Patient History (WIP) ---");
    }
}
