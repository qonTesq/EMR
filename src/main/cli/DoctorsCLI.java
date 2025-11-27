package main.cli;

import java.util.List;
import main.exception.DatabaseException;
import main.exception.EntityNotFoundException;
import main.exception.ValidationException;
import main.model.Doctor;
import main.service.DoctorService;
import main.util.Database;

/**
 * CLI handler for Doctor entity management.
 * <p>
 * This class provides a command-line interface for performing CRUD operations
 * on Doctor records in the EMR system.
 * </p>
 */
public class DoctorsCLI extends CLI {

    private final DoctorService doctorService;

    /**
     * Constructs a new DoctorsCLI with the specified database connection.
     *
     * @param db the database connection to use
     */
    public DoctorsCLI(Database db) {
        super();
        this.doctorService = new DoctorService(db);
    }

    /**
     * Starts the Doctor management CLI interface.
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
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Doctor management menu.
     */
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

    /**
     * Handles the creation of a new doctor.
     */
    private void createDoctor() {
        printSeparator();
        System.out.println("Create New Doctor");

        String id = getRequiredStringInput("Enter Doctor ID: ");
        String name = getRequiredStringInput("Enter Doctor Name: ");

        Doctor doctor = new Doctor(id, name);

        try {
            System.out.println();
            if (doctorService.createDoctor(doctor)) {
                showSuccess("Doctor created successfully");
            } else {
                showError("Failed to create doctor");
            }
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles reading a single doctor by ID.
     */
    private void readDoctor() {
        printSeparator();
        System.out.println("Read Doctor");

        String id = getRequiredStringInput("Enter Doctor ID: ");

        try {
            System.out.println();
            Doctor doctor = doctorService.getDoctorById(id);
            displayDoctor(doctor);
        } catch (EntityNotFoundException e) {
            showNotFound("Doctor", id);
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles reading all doctors from the database.
     */
    private void readAllDoctors() {
        printSeparator();
        System.out.println("Read All Doctors");

        try {
            System.out.println();
            List<Doctor> doctors = doctorService.getAllDoctors();
            if (!doctors.isEmpty()) {
                int count = 1;
                for (Doctor d : doctors) {
                    System.out.println("Doctor " + count + ":");
                    displayDoctor(d);
                    System.out.println();
                    count++;
                }
            } else {
                showEmpty("No doctors found");
            }
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles updating an existing doctor.
     */
    private void updateDoctor() {
        printSeparator();
        System.out.println("Update Doctor");

        String id = getRequiredStringInput("Enter Doctor ID: ");

        Doctor doctor;
        try {
            doctor = doctorService.getDoctorById(id);
        } catch (EntityNotFoundException e) {
            showNotFound("Doctor", id);
            System.out.println();
            return;
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
            System.out.println();
            return;
        }

        System.out.println();
        showInfo("Current doctor name: " + doctor.getName());

        String updatedName = getRequiredStringInput("Enter new doctor name: ");
        doctor.setName(updatedName);

        try {
            System.out.println();
            if (doctorService.updateDoctor(doctor)) {
                showSuccess("Doctor information has been updated");
            } else {
                showError("Update failed");
            }
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (EntityNotFoundException e) {
            showNotFound("Doctor", id);
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles deleting a doctor.
     */
    private void deleteDoctor() {
        printSeparator();
        System.out.println("Delete Doctor");

        String id = getRequiredStringInput("Enter Doctor ID: ");

        Doctor doctor;
        try {
            doctor = doctorService.getDoctorById(id);
        } catch (EntityNotFoundException e) {
            showNotFound("Doctor", id);
            System.out.println();
            return;
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
            System.out.println();
            return;
        }

        System.out.println();
        showInfo("Doctor details:");
        displayDoctor(doctor);

        if (
            getConfirmation(
                "[WARN] Are you sure you want to delete this doctor? (y/n): "
            )
        ) {
            try {
                System.out.println();
                if (doctorService.deleteDoctor(id)) {
                    showSuccess("Doctor deleted successfully");
                } else {
                    showError("Failed to delete doctor");
                }
            } catch (EntityNotFoundException e) {
                showNotFound("Doctor", id);
            } catch (DatabaseException e) {
                showError("Database error: " + e.getMessage());
            }
        } else {
            showCancelled("Deletion cancelled");
        }
        System.out.println();
    }

    /**
     * Displays a doctor's information in a formatted way.
     *
     * @param doctor the doctor to display
     */
    private void displayDoctor(Doctor doctor) {
        System.out.println("ID: " + doctor.getId());
        System.out.println("Name: " + doctor.getName());
    }
}
