package main.cli;

import java.sql.SQLException;
import java.util.List;
import main.dao.ProceduresDAO;
import main.models.Procedures;
import main.util.Database;

public class ProceduresCLI extends CLI {

    private final ProceduresDAO proceduresDAO;

    public ProceduresCLI(Database db) {
        super();
        this.proceduresDAO = new ProceduresDAO(db);
    }

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
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

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

    private void createProcedure() {
        System.out.println("-----");
        System.out.println("Create New Procedure");

        String id = getRequiredStringInput("Enter Procedure ID: ");
        String name = getRequiredStringInput("Enter Procedure Name: ");
        String description = getRequiredStringInput(
            "Enter Procedure Description: "
        );
        int duration = getIntInput("Enter Procedure Duration (minutes): ");
        String doctorId = getRequiredStringInput("Enter Doctor ID: ");

        Procedures procedure = new Procedures(
            id,
            name,
            description,
            duration,
            doctorId
        );

        try {
            System.out.println();
            if (proceduresDAO.createProcedure(procedure)) {
                System.out.println("[OK] Procedure created successfully");
            } else {
                System.out.println("[ERROR] Failed to create procedure");
            }
        } catch (Exception e) {
            System.out.println(
                "[ERROR] Error creating procedure: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void readProcedure() {
        System.out.println("-----");
        System.out.println("Read Procedure");

        String id = getRequiredStringInput("Enter Procedure ID: ");

        try {
            System.out.println();
            Procedures procedure = proceduresDAO.readProcedure(id);
            if (procedure != null) {
                System.out.println("ID: " + procedure.getId());
                System.out.println("Name: " + procedure.getName());
                System.out.println(
                    "Description: " + procedure.getDescription()
                );
                System.out.println("Duration: " + procedure.getDuration());
                System.out.println("Doctor ID: " + procedure.getDoctorId());
            } else {
                System.out.println(
                    "[NOT FOUND] Procedure with ID " + id + " not found"
                );
            }
        } catch (Exception e) {
            System.out.println(
                "[ERROR] Error reading procedure: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void readAllProcedures() {
        System.out.println("-----");
        System.out.println("Read All Procedures");

        try {
            System.out.println();
            List<Procedures> procedures = proceduresDAO.readAllProcedures();
            if (!procedures.isEmpty()) {
                int count = 1;
                for (Procedures p : procedures) {
                    System.out.println("Procedure " + count + ":");
                    System.out.println("ID: " + p.getId());
                    System.out.println("Name: " + p.getName());
                    System.out.println("Description: " + p.getDescription());
                    System.out.println("Duration: " + p.getDuration());
                    System.out.println("Doctor ID: " + p.getDoctorId());
                    System.out.println();
                    count++;
                }
            } else {
                System.out.println("[EMPTY] No procedures found");
            }
        } catch (Exception e) {
            System.out.println(
                "[ERROR] Error reading all procedures: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void updateProcedure() {
        System.out.println("-----");
        System.out.println("Update Procedure");

        String id = getRequiredStringInput("Enter Procedure ID: ");
        Procedures procedures;
        try {
            procedures = proceduresDAO.readProcedure(id);
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
            System.out.println();
            boolean update = proceduresDAO.updateProcedure(procedures);
            if (update) {
                System.out.println(
                    "[OK] Procedure information has been updated"
                );
            } else {
                System.out.println("[ERROR] Update failed");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Update failed: " + e.getMessage());
        }
        System.out.println();
    }

    private void deleteProcedure() {
        System.out.println("-----");
        System.out.println("Delete Procedure");

        String id = getRequiredStringInput("Enter Procedure ID: ");
        Procedures procedure;
        try {
            procedure = proceduresDAO.readProcedure(id);
        } catch (SQLException e) {
            System.out.println(
                "❌ Error fetching procedure: " + e.getMessage()
            );
            System.out.println();
            return;
        }

        if (procedure == null) {
            System.out.println("[NOT FOUND] Procedure not found");
            System.out.println();
            return;
        }

        System.out.println();
        System.out.println("[INFO] Procedure details:");
        System.out.println(procedure.toString());
        String confirm = getStringInput(
            "[WARN] Are you sure you want to delete this procedure? (y/n): "
        );
        if (confirm.equalsIgnoreCase("y")) {
            try {
                System.out.println();
                boolean deleted = proceduresDAO.deleteProcedure(id);
                if (deleted) {
                    System.out.println("[OK] Procedure deleted successfully");
                } else {
                    System.out.println("[ERROR] Failed to delete procedure");
                }
            } catch (SQLException e) {
                System.out.println(
                    "[ERROR] Error deleting procedure: " + e.getMessage()
                );
            }
        } else {
            System.out.println("[CANCELLED] Deletion cancelled");
        }
        System.out.println();
    }
}
