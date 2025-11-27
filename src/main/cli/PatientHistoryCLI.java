package main.cli;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import main.dao.PatientHistoryDAO;
import main.models.PatientHistory;
import main.util.Database;

public class PatientHistoryCLI extends CLI {

    private final PatientHistoryDAO patientHistoryDAO;

    public PatientHistoryCLI(Database db) {
        super();
        this.patientHistoryDAO = new PatientHistoryDAO(db);
    }

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
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

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

    private void createPatientHistory() {
        System.out.println("-----");
        System.out.println("Create New Patient History");

        String id = getRequiredStringInput("Enter History ID: ");
        int patientId = getIntInput("Enter Patient ID: ");
        String procedureId = getRequiredStringInput("Enter Procedure ID: ");
        LocalDate date = getDateInput("Enter Date (yyyy-MM-dd): ");
        double billing = getDoubleInput("Enter Billing Amount: ");
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
            if (patientHistoryDAO.createPatientHistory(patientHistory)) {
                System.out.println("Patient history created successfully");
            } else {
                System.out.println("Failed to create patient history");
            }
        } catch (Exception e) {
            System.out.println(
                "Error creating patient history: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void readPatientHistory() {
        System.out.println("-----");
        System.out.println("Read Patient History");

        String id = getRequiredStringInput("Enter History ID: ");

        try {
            PatientHistory history = patientHistoryDAO.readPatientHistory(id);
            if (history != null) {
                System.out.println(history.toString());
            } else {
                System.out.println(
                    "Patient History with ID " + id + " not found"
                );
            }
        } catch (Exception e) {
            // Handle database errors (e.g., duplicate history ID)
            System.out.println(
                "Error reading Patient History: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void readAllPatientHistories() {
        System.out.println("-----");
        System.out.println("Read All Patient Histories");

        try {
            List<PatientHistory> histories =
                patientHistoryDAO.readAllPatientHistories();
            if (!histories.isEmpty()) {
                for (PatientHistory h : histories) {
                    System.out.println(h.toString());
                }
            } else {
                System.out.println("No patient histories found");
            }
        } catch (Exception e) {
            System.out.println(
                "Error reading all Patient Histories: " + e.getMessage()
            );
        }
    }

    private void updatePatientHistory() {
        System.out.println("-----");
        System.out.println("Update Patient History");

        String patientHistoryID = getRequiredStringInput("Enter History ID: ");
        PatientHistory history;
        try {
            history = patientHistoryDAO.readPatientHistory(patientHistoryID);
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
        System.out.println();
    }

    private void deletePatientHistory() {
        System.out.println("\n--- Delete Patient History (WIP) ---");

        String id = getStringInput("Enter Patient History ID: ");

    try {
        if (patientHistoryDAO.deletePatientHistory(id)) {
            System.out.println("\nPatient history deleted successfully!");
        } else {
            System.out.println("\n!!! Failed to delete patient history !!!");
        }
    } catch (Exception e) {
        // Handle database issues (table not found, wrong column, bad connection, etc.)
        System.out.println("\n!!! Error deleting patient history: " + e.getMessage() + " !!!");
    }
    }
}
