package main.cli;

import java.time.LocalDate;
import java.util.List;
import main.exception.DatabaseException;
import main.exception.EntityNotFoundException;
import main.exception.ValidationException;
import main.model.PatientHistory;
import main.service.PatientHistoryService;
import main.util.Database;

/**
 * CLI handler for PatientHistory entity management.
 * <p>
 * This class provides a command-line interface for performing CRUD operations
 * on PatientHistory records in the EMR system.
 * </p>
 */
public class PatientHistoryCLI extends CLI {

    private final PatientHistoryService patientHistoryService;

    /**
     * Constructs a new PatientHistoryCLI with the specified database connection.
     *
     * @param db the database connection to use
     */
    public PatientHistoryCLI(Database db) {
        super();
        this.patientHistoryService = new PatientHistoryService(db);
    }

    /**
     * Starts the PatientHistory management CLI interface.
     */
    @Override
    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();

            switch (choice) {
                case 1:
                    createPatientHistory();
                    break;
                case 2:
                    readPatientHistory();
                    break;
                case 3:
                    readAllPatientHistories();
                    break;
                case 4:
                    updatePatientHistory();
                    break;
                case 5:
                    deletePatientHistory();
                    break;
                case 6:
                    running = false;
                    System.out.println("Returning to main menu");
                    break;
                default:
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the PatientHistory management menu.
     */
    private void showMenu() {
        System.out.println("Patient History Management");
        System.out.println();
        System.out.println("1. Create Patient History");
        System.out.println("2. Read Patient History by ID");
        System.out.println("3. Read All Patient History");
        System.out.println("4. Update Patient History");
        System.out.println("5. Delete Patient History");
        System.out.println("6. Back to Main Menu");
    }

    /**
     * Handles the creation of a new patient history record.
     */
    private void createPatientHistory() {
        printSeparator();
        System.out.println("Create New Patient History");

        String id = getRequiredStringInput("Enter History ID: ");
        int patientId = getIntInput("Enter Patient MRN: ");
        String procedureId = getRequiredStringInput("Enter Procedure ID: ");
        LocalDate date = getDateInput("Enter Date (yyyy-MM-dd): ");
        double billing = getPositiveDoubleInput("Enter Billing Amount: ");
        String doctorId = getRequiredStringInput("Enter Doctor ID: ");

        PatientHistory patientHistory = new PatientHistory(
            id,
            patientId,
            procedureId,
            date,
            billing,
            doctorId
        );

        try {
            System.out.println();
            if (patientHistoryService.createPatientHistory(patientHistory)) {
                showSuccess("Patient history created successfully");
            } else {
                showError("Failed to create patient history");
            }
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (EntityNotFoundException e) {
            showError(e.getMessage());
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles reading a single patient history by ID.
     */
    private void readPatientHistory() {
        printSeparator();
        System.out.println("Read Patient History");

        String id = getRequiredStringInput("Enter History ID: ");

        try {
            System.out.println();
            PatientHistory history = patientHistoryService.getPatientHistory(
                id
            );
            displayPatientHistory(history);
        } catch (EntityNotFoundException e) {
            showNotFound("Patient History", id);
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles reading all patient histories from the database.
     */
    private void readAllPatientHistories() {
        printSeparator();
        System.out.println("Read All Patient Histories");

        try {
            System.out.println();
            List<PatientHistory> histories =
                patientHistoryService.getAllPatientHistories();
            if (!histories.isEmpty()) {
                int count = 1;
                for (PatientHistory h : histories) {
                    System.out.println("Patient History " + count + ":");
                    displayPatientHistory(h);
                    System.out.println();
                    count++;
                }
            } else {
                showEmpty("No patient histories found");
            }
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles updating an existing patient history record.
     */
    private void updatePatientHistory() {
        printSeparator();
        System.out.println("Update Patient History");

        String id = getRequiredStringInput("Enter History ID: ");

        PatientHistory history;
        try {
            history = patientHistoryService.getPatientHistory(id);
        } catch (EntityNotFoundException e) {
            showNotFound("Patient History", id);
            System.out.println();
            return;
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
            System.out.println();
            return;
        }

        System.out.println();
        showInfo("Current patient history details:");
        displayPatientHistory(history);
        System.out.println();

        String input;

        input = getStringInput("Update Patient MRN (leave empty to skip): ");
        if (!input.isEmpty()) {
            try {
                int patientId = Integer.parseInt(input);
                history.setPatientId(patientId);
            } catch (NumberFormatException e) {
                showError("Invalid Patient MRN. Please enter a valid number.");
                System.out.println();
                return;
            }
        }

        input = getStringInput("Update Procedure ID (leave empty to skip): ");
        if (!input.isEmpty()) {
            history.setProcedureId(input);
        }

        input = getStringInput(
            "Update Date (yyyy-MM-dd, leave empty to skip): "
        );
        if (!input.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                history.setDate(date);
            } catch (Exception e) {
                showError("Invalid date format. Please use yyyy-MM-dd format.");
                System.out.println();
                return;
            }
        }

        input = getStringInput("Update Billing Amount (leave empty to skip): ");
        if (!input.isEmpty()) {
            try {
                double billing = Double.parseDouble(input);
                history.setBilling(billing);
            } catch (NumberFormatException e) {
                showError(
                    "Invalid billing amount. Please enter a valid number."
                );
                System.out.println();
                return;
            }
        }

        input = getStringInput("Update Doctor ID (leave empty to skip): ");
        if (!input.isEmpty()) {
            history.setDoctorId(input);
        }

        try {
            System.out.println();
            if (patientHistoryService.updatePatientHistory(history)) {
                showSuccess("Patient history has been updated");
            } else {
                showError("Update failed");
            }
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (EntityNotFoundException e) {
            showError(e.getMessage());
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles deleting a patient history record.
     */
    private void deletePatientHistory() {
        printSeparator();
        System.out.println("Delete Patient History");

        String id = getRequiredStringInput("Enter History ID: ");

        PatientHistory history;
        try {
            history = patientHistoryService.getPatientHistory(id);
        } catch (EntityNotFoundException e) {
            showNotFound("Patient History", id);
            System.out.println();
            return;
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
            System.out.println();
            return;
        }

        System.out.println();
        showInfo("Patient history details:");
        displayPatientHistory(history);
        System.out.println();

        if (
            getConfirmation(
                "[WARN] Are you sure you want to delete this patient history? (y/n): "
            )
        ) {
            try {
                System.out.println();
                if (patientHistoryService.deletePatientHistory(id)) {
                    showSuccess("Patient history deleted successfully");
                } else {
                    showError("Failed to delete patient history");
                }
            } catch (EntityNotFoundException e) {
                showNotFound("Patient History", id);
            } catch (DatabaseException e) {
                showError("Database error: " + e.getMessage());
            }
        } else {
            showCancelled("Deletion cancelled");
        }
        System.out.println();
    }

    /**
     * Displays a patient history's information in a formatted way.
     *
     * @param history the patient history to display
     */
    private void displayPatientHistory(PatientHistory history) {
        System.out.println("ID: " + history.getId());
        System.out.println("Patient MRN: " + history.getPatientId());
        System.out.println("Procedure ID: " + history.getProcedureId());
        System.out.println("Date: " + history.getDate());
        System.out.println(
            String.format("Billing: $%.2f", history.getBilling())
        );
        System.out.println("Doctor ID: " + history.getDoctorId());
    }
}
