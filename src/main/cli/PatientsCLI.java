package main.cli;

import java.sql.SQLException;
import java.time.LocalDate;
import main.dao.PatientDAO;
import main.models.Patients;
import main.util.Database;

/**
 * Command Line Interface for managing patient records in the EMR system.
 * <p>
 * This class provides a comprehensive menu-driven interface for performing
 * CRUD (Create, Read, Update, Delete) operations on patient data. It handles
 * patient demographic information, contact details, and insurance information.
 * </p>
 *
 * <h3>Features:</h3>
 * <ul>
 * <li>Create new patient records with complete demographic information</li>
 * <li>Search and retrieve patient information by MRN</li>
 * <li>View all patients in the system</li>
 * <li>Update existing patient information</li>
 * <li>Delete patient records</li>
 * </ul>
 *
 * @see PatientDAO
 * @see Patients
 */
public class PatientsCLI extends CLI {

    /** Data Access Object for performing database operations on patient records */
    private final PatientDAO patientDAO;

    /**
     * Constructs a new PatientsCLI with the specified database connection.
     * <p>
     * Initializes the patient data access object for database operations.
     * </p>
     *
     * @param db the Database instance for database operations
     * @throws NullPointerException if db is null
     */
    public PatientsCLI(Database db) {
        super();
        this.patientDAO = new PatientDAO(db);
    }

    /**
     * Starts the patient management interface and enters the interaction loop.
     * <p>
     * This method displays the patient management menu and processes user choices
     * until the user chooses to return to the main menu. Each menu option delegates
     * to a specific method for handling that operation.
     * </p>
     *
     * <h3>Menu Options:</h3>
     * <ol>
     * <li>Create Patient - Add a new patient record</li>
     * <li>Read Patient by MRN - Look up a specific patient</li>
     * <li>Read All Patients - List all patients in the system</li>
     * <li>Update Patient - Modify existing patient information</li>
     * <li>Delete Patient - Remove a patient record</li>
     * <li>Back to Main Menu - Return to main application menu</li>
     * </ol>
     */
    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createPatient();
                    break;
                case 2:
                    readPatient();
                    break;
                case 3:
                    readAllPatients();
                    break;
                case 4:
                    updatePatient();
                    break;
                case 5:
                    deletePatient();
                    break;
                case 6:
                    running = false;
                    System.out.println("\nReturning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the patient management menu options.
     * Shows all available operations for patient management.
     */
    private void showMenu() {
        System.out.println("\n=== Patient Management System ===");
        System.out.println("1. Create Patient");
        System.out.println("2. Read Patient by MRN");
        System.out.println("3. Read All Patients");
        System.out.println("4. Update Patient");
        System.out.println("5. Delete Patient");
        System.out.println("6. Back to Main Menu");
        System.out.println("=================================");
    }

    /**
     * Handles the creation of a new patient record with complete demographic
     * information.
     * <p>
     * This method guides the user through entering all required patient
     * information,
     * including personal details, address, and insurance information. All inputs
     * are
     * validated before creating the patient record. The method provides immediate
     * feedback on success or failure of the operation.
     * </p>
     *
     * <h3>Required Information:</h3>
     * <ul>
     * <li><b>MRN</b> - Medical Record Number (unique identifier)</li>
     * <li><b>First Name</b> - Patient's given name</li>
     * <li><b>Last Name</b> - Patient's family name</li>
     * <li><b>Date of Birth</b> - In yyyy-MM-dd format</li>
     * <li><b>Address</b> - Street address</li>
     * <li><b>State</b> - State of residence</li>
     * <li><b>City</b> - City of residence</li>
     * <li><b>Zip Code</b> - Postal code</li>
     * <li><b>Insurance</b> - Insurance provider information</li>
     * </ul>
     *
     * @see PatientDAO#createPatient(Patients)
     */
    private void createPatient() {
        System.out.println("\n--- Create New Patient ---");

        // Collect all required patient information with validation
        int mrn = getIntInput("Enter MRN: ");
        String fname = getRequiredStringInput("Enter First Name: ");
        String lname = getRequiredStringInput("Enter Last Name: ");
        LocalDate dob = getDateInput("Enter Date of Birth (yyyy-MM-dd): ");
        String address = getRequiredStringInput("Enter Address: ");
        String state = getRequiredStringInput("Enter State: ");
        String city = getRequiredStringInput("Enter City: ");
        int zip = getIntInput("Enter Zip Code: ");
        String insurance = getRequiredStringInput("Enter Insurance: ");
        String email = getRequiredStringInput("Enter Email: ");

        // Create patient object with collected data
        Patients patient = new Patients(
            mrn,
            fname,
            lname,
            dob,
            address,
            state,
            city,
            zip,
            insurance,
            email
        );

        // Attempt to save patient to database
        try {
            if (patientDAO.createPatient(patient)) {
                System.out.println("\nPatient created successfully!");
            } else {
                System.out.println("\n!!! Failed to create patient !!!");
            }
        } catch (Exception e) {
            // Handle database errors (e.g., duplicate MRN, connection issues)
            System.out.println(
                "\n!!! Error creating patient: " + e.getMessage() + " !!!"
            );
        }
    }

    private void readPatient() {
        System.out.println("\n--- Read Patient (WIP) ---");
    }

    private void readAllPatients() {
        System.out.println("\n--- All Patients (WIP) ---");
    }

    private void updatePatient() {
        System.out.println("\n--- Update Patient ---");

        int mrn = getIntInput("Enter Patient MRN: ");
        Patients patient = patientDAO.getPatientMRN(mrn);

        if (patient == null) {
            System.out.println("Patient not found");
            return;
        }

        String input;

        input = getStringInput("Update last name (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setLname(input);
        }

        input = getStringInput("Update first name (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setFname(input);
        }

        input = getStringInput(
            "Update Date of Birth (yyyy-MM-dd, leave empty to skip): "
        );
        if (!input.isEmpty()) {
            try {
                LocalDate dob = LocalDate.parse(input, DATE_FORMATTER);
                patient.setDob(dob);
            } catch (Exception e) {
                System.out.println(
                    "Invalid date format. Please use yyyy-MM-dd format."
                );
                return;
            }
        }

        input = getStringInput("Update Address (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setAddress(input);
        }

        input = getStringInput("Update State (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setState(input);
        }

        input = getStringInput("Update City (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setCity(input);
        }

        input = getStringInput("Update ZipCode (leave empty to skip): ");
        if (!input.isEmpty()) {
            try {
                patient.setZip(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid zip code. Please enter a valid number."
                );
                return;
            }
        }

        input = getStringInput("Update email (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setEmail(input);
        }

        input = getStringInput("Update Insurance (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setInsurance(input);
        }

        try {
            boolean update = patientDAO.updatePatient(patient);
            if (update) {
                System.out.println("Patient information has been updated");
            } else {
                System.out.println("Update failed");
            }
        } catch (SQLException e) {
            System.out.println("Error updating patient: " + e.getMessage());
        }
    }

    private void deletePatient() {
        System.out.println("\n--- Delete Patient (WIP) ---");
    }
}
