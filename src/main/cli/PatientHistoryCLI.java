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
            System.out.println();
            if (patientHistoryDAO.createPatientHistory(patientHistory)) {
                System.out.println("[OK] Patient history created successfully");
            } else {
                System.out.println("[ERROR] Failed to create patient history");
            }
        } catch (Exception e) {
            System.out.println(
                "[ERROR] Error creating patient history: " + e.getMessage()
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
            System.out.println();
            if (history != null) {
                System.out.println("ID: " + history.getId());
                System.out.println("Patient ID: " + history.getPatientId());
                System.out.println("Procedure ID: " + history.getProcedureId());
                System.out.println("Date: " + history.getDate());
                System.out.println("Billing: " + history.getBilling());
                System.out.println("Doctor ID: " + history.getDoctorId());
            } else {
                System.out.println(
                    "[NOT FOUND] Patient History with ID " + id + " not found"
                );
            }
        } catch (Exception e) {
            System.out.println(
                "[ERROR] Error reading Patient History: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void readAllPatientHistories() {
        System.out.println("-----");
        System.out.println("Read All Patient Histories");

        try {
            System.out.println();
            List<PatientHistory> histories =
                patientHistoryDAO.readAllPatientHistories();
            if (!histories.isEmpty()) {
                int count = 1;
                for (PatientHistory h : histories) {
                    System.out.println("Patient History " + count + ":");
                    System.out.println("ID: " + h.getId());
                    System.out.println("Patient ID: " + h.getPatientId());
                    System.out.println("Procedure ID: " + h.getProcedureId());
                    System.out.println("Date: " + h.getDate());
                    System.out.println("Billing: " + h.getBilling());
                    System.out.println("Doctor ID: " + h.getDoctorId());
                    System.out.println();
                    count++;
                }
            } else {
                System.out.println("[EMPTY] No patient histories found");
            }
        } catch (Exception e) {
            System.out.println(
                "[ERROR] Error reading all Patient Histories: " + e.getMessage()
            );
        }
        System.out.println();
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
            System.out.println();
            boolean update = patientHistoryDAO.updatePatientHistory(history);
            if (update) {
                System.out.println("[OK] Patient history has been updated");
            } else {
                System.out.println("[ERROR] Update failed");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Update failed: " + e.getMessage());
        }
        System.out.println();
    }

    private void deletePatientHistory() {
        System.out.println("-----");
        System.out.println("Delete Patient History");

        String id = getRequiredStringInput("Enter History ID: ");
        PatientHistory history;
        try {
            history = patientHistoryDAO.readPatientHistory(id);
        } catch (SQLException e) {
            System.out.println(
                "[ERROR] Error fetching patient history: " + e.getMessage()
            );
            System.out.println();
            return;
        }

        if (history == null) {
            System.out.println("[NOT FOUND] Patient history not found");
            System.out.println();
            return;
        }

        System.out.println();
        System.out.println("[INFO] Patient history details:");
        System.out.println(history.toString());
        String confirm = getStringInput(
            "[WARN] Are you sure you want to delete this patient history? (y/n): "
        );
        if (confirm.equalsIgnoreCase("y")) {
            try {
                System.out.println();
                boolean deleted = patientHistoryDAO.deletePatientHistory(id);
                if (deleted) {
                    System.out.println(
                        "[OK] Patient history deleted successfully"
                    );
                } else {
                    System.out.println(
                        "[ERROR] Failed to delete patient history"
                    );
                }
            } catch (SQLException e) {
                System.out.println(
                    "[ERROR] Error deleting patient history: " + e.getMessage()
                );
            }
        } else {
            System.out.println("[CANCELLED] Deletion cancelled");
        }
        System.out.println();
    }
}
