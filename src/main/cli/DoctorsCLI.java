package main.cli;

import java.sql.SQLException;
import java.util.List;
import main.dao.DoctorDAO;
import main.models.Doctors;
import main.util.Database;

public class DoctorsCLI extends CLI {

    private final DoctorDAO doctorDAO;

    public DoctorsCLI(Database db) {
        super();
        this.doctorDAO = new DoctorDAO(db);
    }

    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();

            switch (choice) {
                case 1:
                    createDoctor();
                    break;
                case 2:
                    readDoctor();
                    break;
                case 3:
                    readAllDoctors();
                    break;
                case 4:
                    updateDoctor();
                    break;
                case 5:
                    deleteDoctor();
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
        System.out.println("Doctor Management");
        System.out.println();
        System.out.println("1. Create Doctor");
        System.out.println("2. Read Doctor by ID");
        System.out.println("3. Read All Doctors");
        System.out.println("4. Update Doctor");
        System.out.println("5. Delete Doctor");
        System.out.println("6. Back to Main Menu");
    }

    private void createDoctor() {
        System.out.println("-----");
        System.out.println("Create New Doctor");

        String id = getRequiredStringInput("Enter Doctor ID: ");
        String name = getRequiredStringInput("Enter Doctor Name: ");

        Doctors doctor = new Doctors(id, name);

        try {
            System.out.println();
            if (doctorDAO.createDoctor(doctor)) {
                System.out.println("[OK] Doctor created successfully");
            } else {
                System.out.println("[ERROR] Failed to create doctor");
            }
        } catch (Exception e) {
            System.out.println(
                "[ERROR] Error creating doctor: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void readDoctor() {
        System.out.println("-----");
        System.out.println("Read Doctor");

        String id = getRequiredStringInput("Enter Doctor ID: ");

        try {
            System.out.println();
            Doctors doctor = doctorDAO.readDoctor(id);
            if (doctor != null) {
                System.out.println("ID: " + doctor.getId());
                System.out.println("Name: " + doctor.getName());
            } else {
                System.out.println(
                    "[NOT FOUND] Doctor with ID " + id + " not found"
                );
            }
        } catch (Exception e) {
            System.out.println(
                "[ERROR] Error reading doctor: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void readAllDoctors() {
        System.out.println("-----");
        System.out.println("Read All Doctors");

        try {
            System.out.println();
            List<Doctors> doctors = doctorDAO.readAllDoctors();
            if (!doctors.isEmpty()) {
                int count = 1;
                for (Doctors d : doctors) {
                    System.out.println("Doctor " + count + ":");
                    System.out.println("ID: " + d.getId());
                    System.out.println("Name: " + d.getName());
                    System.out.println();
                    count++;
                }
            } else {
                System.out.println("[EMPTY] No doctors found");
            }
        } catch (Exception e) {
            System.out.println(
                "[ERROR] Error reading all doctors: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void updateDoctor() {
        System.out.println("-----");
        System.out.println("Update Doctor");

        String id = getRequiredStringInput("Enter Doctor ID: ");
        if (!id.startsWith("DR")) {
            System.out.println("Invalid ID");
            return;
        }

        Doctors doctor;
        try {
            doctor = doctorDAO.readDoctor(id);
        } catch (SQLException e) {
            System.out.println("Error fetching records: " + e.getMessage());
            return;
        }

        if (doctor == null) {
            System.out.println("Doctor not found");
            return;
        }

        String updatedDoctor = getRequiredStringInput(
            "Enter new doctor name: "
        );
        doctor.setName(updatedDoctor);

        try {
            System.out.println();
            boolean update = doctorDAO.updateDoctor(doctor);
            if (update) {
                System.out.println("[OK] Doctor information has been updated");
            } else {
                System.out.println("[ERROR] Update failed");
            }
        } catch (SQLException e) {
            System.out.println(
                "[ERROR] Error updating doctor: " + e.getMessage()
            );
        }
        System.out.println();
    }

    private void deleteDoctor() {
        System.out.println("-----");
        System.out.println("Delete Doctor");

        String id = getRequiredStringInput("Enter Doctor ID: ");
        Doctors doctor;
        try {
            doctor = doctorDAO.readDoctor(id);
        } catch (SQLException e) {
            System.out.println(
                "[ERROR] Error fetching doctor: " + e.getMessage()
            );
            System.out.println();
            return;
        }

        if (doctor == null) {
            System.out.println("[NOT FOUND] Doctor not found");
            System.out.println();
            return;
        }

        System.out.println();
        System.out.println("[INFO] Doctor details:");
        System.out.println(doctor.toString());
        String confirm = getStringInput(
            "[WARN] Are you sure you want to delete this doctor? (y/n): "
        );
        if (confirm.equalsIgnoreCase("y")) {
            try {
                System.out.println();
                boolean deleted = doctorDAO.deleteDoctor(id);
                if (deleted) {
                    System.out.println("[OK] Doctor deleted successfully");
                } else {
                    System.out.println("[ERROR] Failed to delete doctor");
                }
            } catch (SQLException e) {
                System.out.println(
                    "[ERROR] Error deleting doctor: " + e.getMessage()
                );
            }
        } else {
            System.out.println("[CANCELLED] Deletion cancelled");
        }
        System.out.println();
    }
}
