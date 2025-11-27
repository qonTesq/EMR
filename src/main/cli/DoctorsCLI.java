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

    /**
     * Starts the doctor management interface and enters the interaction loop.
     * <p>
     * Displays the main menu and processes user selections in a loop until
     * the user chooses to exit.
     * </p>
     */
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
            if (doctorDAO.createDoctor(doctor)) {
                System.out.println("Doctor created successfully");
            } else {
                System.out.println("Failed to create doctor");
            }
        } catch (Exception e) {
            System.out.println("Error creating doctor: " + e.getMessage());
        }
        System.out.println();
    }

    private void readDoctor() {
        System.out.println("-----");
        System.out.println("Read Doctor");

        String id = getRequiredStringInput("Enter Doctor ID: ");

        try {
            Doctors doctor = doctorDAO.readDoctor(id);
            if (doctor != null) {
                System.out.println(doctor.toString());
            } else {
                System.out.println("Doctor with ID " + id + " not found");
            }
        } catch (Exception e) {
            System.out.println("Error reading doctor: " + e.getMessage());
        }
        System.out.println();
    }

    private void readAllDoctors() {
        System.out.println("-----");
        System.out.println("Read All Doctors");

        try {
            List<Doctors> doctors = doctorDAO.readAllDoctors();
            if (!doctors.isEmpty()) {
                for (Doctors d : doctors) {
                    System.out.println(d.toString());
                }
            } else {
                System.out.println("No doctors found");
            }
        } catch (Exception e) {
            System.out.println("Error reading all doctors: " + e.getMessage());
        }
        System.out.println();
    }

    private void deleteDoctor(){
        System.out.println("\n--- Delete Doctor ---");

        String id = getStringInput("Enter Doctor ID to delete (e.g., DR1): ");

        try {
            boolean deleted = doctorDAO.deleteDoctor(id);

            if (deleted) {
                System.out.println("\nDoctor deleted successfully!");
            } else {
                System.out.println("\n!!! Failed to delete doctor (ID not found) !!!");
            }

        } catch (Exception e) {
            System.out.println("\n!!! Error deleting doctor: " + e.getMessage() + " !!!");
        }
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

        boolean update = doctorDAO.updateDoctor(doctor);
        if (update) {
            System.out.println("Doctor information has been updated");
        } else {
            System.out.println("Update failed");
        }
        System.out.println();
    }

    private void deleteDoctor() {
        System.out.println("-----");
        System.out.println("Delete Doctor (WIP)");
        System.out.println();
    }
}
