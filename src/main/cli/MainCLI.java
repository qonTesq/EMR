package main.cli;

import java.util.Scanner;

import main.util.Database;

/**
 * Main Command Line Interface for the Electronic Medical Records (EMR)
 * Management System.
 * This class provides the primary navigation and menu system for the
 * application,
 * allowing users to choose which entity type they want to manage.
 *
 * The main CLI serves as the entry point for user interaction and delegates
 * specific entity management to specialized CLI classes (PatientsCLI,
 * PatientHistoryCLI, ProceduresCLI).
 */
public class MainCLI {

    /** Database connection utility instance */
    private Database db;

    /** Scanner for reading user input from console */
    private Scanner scanner;

    /**
     * Constructs a new MainCLI with the specified database connection.
     *
     * @param db the Database utility instance for managing connections
     */
    public MainCLI(Database db) {
        this.db = db;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main CLI interface and displays the welcome message.
     * This method runs the main application loop, presenting the menu
     * and handling user choices until the user chooses to exit.
     */
    public void start() {
        // Display welcome message
        System.out.println("=== Welcome to EMR Management System ===\n");

        boolean running = true;
        while (running) {
            // Display main menu and get user choice
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");

            // Process user choice
            switch (choice) {
                case 1:
                    managePatients();
                    break;
                case 2:
                    managePatientHistory();
                    break;
                case 3:
                    manageProcedures();
                    break;
                case 4:
                    running = false;
                    System.out.println("\nThank you for using EMR Management System. Goodbye!\n");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the main menu options to the user.
     * Shows available entity types that can be managed and the exit option.
     */
    private void showMainMenu() {
        System.out.println("=== EMR Management System ===");
        System.out.println("Choose an entity to manage:");
        System.out.println("1. Patients");
        System.out.println("2. Patient History");
        System.out.println("3. Procedures");
        System.out.println("4. Exit");
        System.out.println("=============================");
    }

    /**
     * Launches the Patients management interface.
     * Creates and starts a PatientsCLI instance to handle patient-related
     * operations.
     */
    private void managePatients() {
        System.out.println("\n--- Managing Patients ---");
        PatientsCLI patientCLI = new PatientsCLI(db);
        patientCLI.start();
    }

    /**
     * Launches the Patient History management interface.
     * Creates and starts a PatientHistoryCLI instance to handle patient history
     * operations.
     */
    private void managePatientHistory() {
        System.out.println("\n--- Managing Patient History ---");
        PatientHistoryCLI patientHistoryCLI = new PatientHistoryCLI(db);
        patientHistoryCLI.start();
    }

    /**
     * Launches the Procedures management interface.
     * Creates and starts a ProceduresCLI instance to handle procedure-related
     * operations.
     */
    private void manageProcedures() {
        System.out.println("\n--- Managing Procedures ---");
        ProceduresCLI proceduresCLI = new ProceduresCLI(db);
        proceduresCLI.start();
    }

    /**
     * Reads and validates integer input from the user.
     * Continuously prompts the user until a valid integer is entered.
     *
     * @param prompt the message to display when asking for input
     * @return the validated integer input
     */
    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}