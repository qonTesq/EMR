package main.cli;

import java.util.List;
import main.exception.DatabaseException;
import main.exception.EntityNotFoundException;
import main.exception.ValidationException;
import main.model.Procedure;
import main.service.ProcedureService;
import main.util.Database;

/**
 * CLI handler for Procedure entity management.
 * <p>
 * This class provides a command-line interface for performing CRUD operations
 * on Procedure records in the EMR system.
 * </p>
 */
public class ProceduresCLI extends CLI {

    private final ProcedureService procedureService;

    /**
     * Constructs a new ProceduresCLI with the specified database connection.
     *
     * @param db the database connection to use
     */
    public ProceduresCLI(Database db) {
        super();
        this.procedureService = new ProcedureService(db);
    }

    /**
     * Starts the Procedure management CLI interface.
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
                    System.out.println("Returning to main menu");
                    break;
                default:
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Procedure management menu.
     */
    private void showMenu() {
        System.out.println("Procedures Management");
        System.out.println();
        System.out.println("1. Create Procedure");
        System.out.println("2. Read Procedure by ID");
        System.out.println("3. Read All Procedures");
        System.out.println("4. Update Procedure");
        System.out.println("5. Delete Procedure");
        System.out.println("6. Back to Main Menu");
    }

    /**
     * Handles the creation of a new procedure.
     */
    private void createProcedure() {
        printSeparator();
        System.out.println("Create New Procedure");

        String id = getRequiredStringInput("Enter Procedure ID: ");
        String name = getRequiredStringInput("Enter Procedure Name: ");
        String description = getRequiredStringInput(
            "Enter Procedure Description: "
        );
        int duration = getIntInput("Enter Procedure Duration (minutes): ");
        String doctorId = getRequiredStringInput("Enter Doctor ID: ");

        Procedure procedure = new Procedure(
            id,
            name,
            description,
            duration,
            doctorId
        );

        try {
            System.out.println();
            if (procedureService.createProcedure(procedure)) {
                showSuccess("Procedure created successfully");
            } else {
                showError("Failed to create procedure");
            }
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles reading a single procedure by ID.
     */
    private void readProcedure() {
        printSeparator();
        System.out.println("Read Procedure");

        String id = getRequiredStringInput("Enter Procedure ID: ");

        try {
            System.out.println();
            Procedure procedure = procedureService.getProcedure(id);
            displayProcedure(procedure);
        } catch (EntityNotFoundException e) {
            showNotFound("Procedure", id);
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles reading all procedures from the database.
     */
    private void readAllProcedures() {
        printSeparator();
        System.out.println("Read All Procedures");

        try {
            System.out.println();
            List<Procedure> procedures = procedureService.getAllProcedures();
            if (!procedures.isEmpty()) {
                int count = 1;
                for (Procedure p : procedures) {
                    System.out.println("Procedure " + count + ":");
                    displayProcedure(p);
                    System.out.println();
                    count++;
                }
            } else {
                showEmpty("No procedures found");
            }
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles updating an existing procedure.
     */
    private void updateProcedure() {
        printSeparator();
        System.out.println("Update Procedure");

        String id = getRequiredStringInput("Enter Procedure ID: ");

        Procedure procedure;
        try {
            procedure = procedureService.getProcedure(id);
        } catch (EntityNotFoundException e) {
            showNotFound("Procedure", id);
            System.out.println();
            return;
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
            System.out.println();
            return;
        }

        System.out.println();
        showInfo("Current procedure details:");
        displayProcedure(procedure);
        System.out.println();

        String input;

        input = getStringInput("Update procedure name (leave empty to skip): ");
        if (!input.isEmpty()) {
            procedure.setName(input);
        }

        input = getStringInput(
            "Update procedure description (leave empty to skip): "
        );
        if (!input.isEmpty()) {
            procedure.setDescription(input);
        }

        input = getStringInput(
            "Update procedure duration in minutes (leave empty to skip): "
        );
        if (!input.isEmpty()) {
            try {
                int duration = Integer.parseInt(input);
                procedure.setDuration(duration);
            } catch (NumberFormatException e) {
                showError("Invalid duration. Please enter a valid number.");
                System.out.println();
                return;
            }
        }

        input = getStringInput("Update Doctor ID (leave empty to skip): ");
        if (!input.isEmpty()) {
            procedure.setDoctorId(input);
        }

        try {
            System.out.println();
            if (procedureService.updateProcedure(procedure)) {
                showSuccess("Procedure information has been updated");
            } else {
                showError("Update failed");
            }
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (EntityNotFoundException e) {
            showNotFound("Procedure", id);
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles deleting a procedure.
     */
    private void deleteProcedure() {
        printSeparator();
        System.out.println("Delete Procedure");

        String id = getRequiredStringInput("Enter Procedure ID: ");

        Procedure procedure;
        try {
            procedure = procedureService.getProcedure(id);
        } catch (EntityNotFoundException e) {
            showNotFound("Procedure", id);
            System.out.println();
            return;
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
            System.out.println();
            return;
        }

        System.out.println();
        showInfo("Procedure details:");
        displayProcedure(procedure);
        System.out.println();

        if (
            getConfirmation(
                "[WARN] Are you sure you want to delete this procedure? (y/n): "
            )
        ) {
            try {
                System.out.println();
                if (procedureService.deleteProcedure(id)) {
                    showSuccess("Procedure deleted successfully");
                } else {
                    showError("Failed to delete procedure");
                }
            } catch (EntityNotFoundException e) {
                showNotFound("Procedure", id);
            } catch (DatabaseException e) {
                showError("Database error: " + e.getMessage());
            }
        } else {
            showCancelled("Deletion cancelled");
        }
        System.out.println();
    }

    /**
     * Displays a procedure's information in a formatted way.
     *
     * @param procedure the procedure to display
     */
    private void displayProcedure(Procedure procedure) {
        System.out.println("ID: " + procedure.getId());
        System.out.println("Name: " + procedure.getName());
        System.out.println("Description: " + procedure.getDescription());
        System.out.println("Duration: " + procedure.getDuration() + " minutes");
        System.out.println("Doctor ID: " + procedure.getDoctorId());
    }
}
