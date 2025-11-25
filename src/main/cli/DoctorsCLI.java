package main.cli;

import java.sql.SQLException;
import main.dao.DoctorDAO;
import main.models.Doctors;
import main.util.Database;

/**
 * Command Line Interface for managing doctor records in the EMR system.
 * <p>
 * This class provides an interface for managing doctor information in the
 * Electronic Medical Records system. Doctors are medical professionals who
 * perform Doctors on patients.
 * </p>
 *
 * <h3>Features:</h3>
 * <ul>
 * <li>Create new doctor records</li>
 * </ul>
 *
 * @see DoctorDAO
 * @see Doctors
 */
public class DoctorsCLI extends CLI {

    /** Data Access Object for performing database operations on doctor records */
    private final DoctorDAO doctorDAO;

    /**
     * Constructs a new DoctorsCLI with the specified database connection.
     * <p>
     * Initializes the doctor data access object for database operations.
     * </p>
     *
     * @param db the Database instance for database operations
     * @throws NullPointerException if db is null
     */
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
        while (true) {
            System.out.println("\n=== Doctor Management ===");
            System.out.println("1. Create Doctor");
            System.out.println("2. Update Doctor");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = getIntInput("");

            switch (choice) {
                case 1:
                    createDoctor();
                    break;
                case 2:
                    updateDoctor();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showMenu() 
    {
        System.out.println("\n=== Doctor Management ===");
            System.out.println("1. Create Doctor");
            System.out.println("2. Read Doctor by ID");
            System.out.println("3. Read All Doctors");
            System.out.println("4. Update Doctor");
            System.out.println("5. Delete Doctor");
            System.out.println("6. Back to Main Menu");
            System.out.println("============================");
            System.out.print("Choose an option: ");
    }

    /**
     * Creates a new doctor record in the database.
     * <p>
     * Prompts the user for doctor information including ID and name,
     * validates the input, and attempts to save the doctor to the database.
     * </p>
     */
    private void createDoctor() {
        System.out.println("\n--- Create New Doctor ---");

        // Collect doctor information with validation
        String id = getRequiredStringInput("Enter Doctor ID: ");
        String name = getRequiredStringInput("Enter Doctor Name: ");

        // Create doctor object with collected data
        Doctors doctor = new Doctors(id, name);

        // Attempt to save doctor to database
        try {
            if (doctorDAO.createDoctor(doctor)) {
                System.out.println("Doctor created successfully!");
            } else {
                System.out.println("Failed to create doctor.");
            }
        } catch (Exception e) {
            // Handle database errors (e.g., duplicate doctor ID)
            System.out.println("Error creating doctor: " + e.getMessage());
        }
    }

    private void updateDoctor() {
        System.out.println("\n--- Update Doctor ---");

        String id = getRequiredStringInput("Enter Doctor ID: ");
        if (!id.startsWith("DR")) {
            System.out.println("Invalid ID.");
            return;
        }

        Doctors doctor;
        try {
            doctor = doctorDAO.getDoctor(id);
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
    }
}
