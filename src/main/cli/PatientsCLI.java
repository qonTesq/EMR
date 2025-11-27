package main.cli;

import java.time.LocalDate;
import java.util.List;
import main.exception.DatabaseException;
import main.exception.EntityNotFoundException;
import main.exception.ValidationException;
import main.model.Patient;
import main.service.PatientService;
import main.util.Database;

/**
 * CLI handler for Patient entity management.
 * <p>
 * This class provides a command-line interface for performing CRUD operations
 * on Patient records in the EMR system.
 * </p>
 */
public class PatientsCLI extends CLI {

    private final PatientService patientService;

    /**
     * Constructs a new PatientsCLI with the specified database connection.
     *
     * @param db the database connection to use
     */
    public PatientsCLI(Database db) {
        super();
        this.patientService = new PatientService(db);
    }

    /**
     * Starts the Patient management CLI interface.
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
                    System.out.println("Returning to main menu");
                    break;
                default:
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Patient management menu.
     */
    private void showMenu() {
        System.out.println("Patient Management");
        System.out.println();
        System.out.println("1. Create Patient");
        System.out.println("2. Read Patient by MRN");
        System.out.println("3. Read All Patients");
        System.out.println("4. Update Patient");
        System.out.println("5. Delete Patient");
        System.out.println("6. Back to Main Menu");
    }

    /**
     * Handles the creation of a new patient.
     */
    private void createPatient() {
        printSeparator();
        System.out.println("Create New Patient");

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

        Patient patient = new Patient(
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

        try {
            System.out.println();
            if (patientService.createPatient(patient)) {
                showSuccess("Patient created successfully");
            } else {
                showError("Failed to create patient");
            }
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles reading a single patient by MRN.
     */
    private void readPatient() {
        printSeparator();
        System.out.println("Read Patient");

        int mrn = getIntInput("Enter MRN: ");

        try {
            System.out.println();
            Patient patient = patientService.getPatient(mrn);
            displayPatient(patient);
        } catch (EntityNotFoundException e) {
            showNotFound("Patient", mrn);
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles reading all patients from the database.
     */
    private void readAllPatients() {
        printSeparator();
        System.out.println("Read All Patients");

        try {
            System.out.println();
            List<Patient> patients = patientService.getAllPatients();
            if (!patients.isEmpty()) {
                int count = 1;
                for (Patient p : patients) {
                    System.out.println("Patient " + count + ":");
                    displayPatient(p);
                    System.out.println();
                    count++;
                }
            } else {
                showEmpty("No patients found");
            }
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles updating an existing patient.
     */
    private void updatePatient() {
        printSeparator();
        System.out.println("Update Patient");

        int mrn = getIntInput("Enter Patient MRN: ");

        Patient patient;
        try {
            patient = patientService.getPatient(mrn);
        } catch (EntityNotFoundException e) {
            showNotFound("Patient", mrn);
            System.out.println();
            return;
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
            System.out.println();
            return;
        }

        System.out.println();
        showInfo("Current patient details:");
        displayPatient(patient);
        System.out.println();

        String input;

        input = getStringInput("Update first name (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setFname(input);
        }

        input = getStringInput("Update last name (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setLname(input);
        }

        input = getStringInput(
            "Update Date of Birth (yyyy-MM-dd, leave empty to skip): "
        );
        if (!input.isEmpty()) {
            try {
                LocalDate dob = LocalDate.parse(input, DATE_FORMATTER);
                patient.setDob(dob);
            } catch (Exception e) {
                showError("Invalid date format. Please use yyyy-MM-dd format.");
                System.out.println();
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

        input = getStringInput("Update Zip Code (leave empty to skip): ");
        if (!input.isEmpty()) {
            try {
                int zip = Integer.parseInt(input);
                patient.setZip(zip);
            } catch (NumberFormatException e) {
                showError("Invalid zip code. Please enter a valid number.");
                System.out.println();
                return;
            }
        }

        input = getStringInput("Update Insurance (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setInsurance(input);
        }

        input = getStringInput("Update Email (leave empty to skip): ");
        if (!input.isEmpty()) {
            patient.setEmail(input);
        }

        try {
            System.out.println();
            if (patientService.updatePatient(patient)) {
                showSuccess("Patient information has been updated");
            } else {
                showError("Update failed");
            }
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (EntityNotFoundException e) {
            showNotFound("Patient", mrn);
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Handles deleting a patient.
     */
    private void deletePatient() {
        printSeparator();
        System.out.println("Delete Patient");

        int mrn = getIntInput("Enter MRN: ");

        Patient patient;
        try {
            patient = patientService.getPatient(mrn);
        } catch (EntityNotFoundException e) {
            showNotFound("Patient", mrn);
            System.out.println();
            return;
        } catch (DatabaseException e) {
            showError("Database error: " + e.getMessage());
            System.out.println();
            return;
        }

        System.out.println();
        showInfo("Patient details:");
        displayPatient(patient);
        System.out.println();

        if (
            getConfirmation(
                "[WARN] Are you sure you want to delete this patient? (y/n): "
            )
        ) {
            try {
                System.out.println();
                if (patientService.deletePatient(mrn)) {
                    showSuccess("Patient deleted successfully");
                } else {
                    showError("Failed to delete patient");
                }
            } catch (EntityNotFoundException e) {
                showNotFound("Patient", mrn);
            } catch (DatabaseException e) {
                showError("Database error: " + e.getMessage());
            }
        } else {
            showCancelled("Deletion cancelled");
        }
        System.out.println();
    }

    /**
     * Displays a patient's information in a formatted way.
     *
     * @param patient the patient to display
     */
    private void displayPatient(Patient patient) {
        System.out.println("MRN: " + patient.getMrn());
        System.out.println(
            "Name: " + patient.getFname() + " " + patient.getLname()
        );
        System.out.println("DOB: " + patient.getDob());
        System.out.println("Address: " + patient.getAddress());
        System.out.println("City: " + patient.getCity());
        System.out.println("State: " + patient.getState());
        System.out.println("Zip: " + patient.getZip());
        System.out.println("Insurance: " + patient.getInsurance());
        System.out.println("Email: " + patient.getEmail());
    }
}
