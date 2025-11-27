package main.cli;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import main.dao.PatientDAO;
import main.models.Patients;
import main.util.Database;

public class PatientsCLI extends CLI {

    private final PatientDAO patientDAO;

    public PatientsCLI(Database db) {
        super();
        this.patientDAO = new PatientDAO(db);
    }

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
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

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

    private void createPatient() {
        System.out.println("-----");
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

        try {
            if (patientDAO.createPatient(patient)) {
                System.out.println("Patient created successfully");
            } else {
                System.out.println("Failed to create patient");
            }
        } catch (Exception e) {
            System.out.println("Error creating patient: " + e.getMessage());
        }
        System.out.println();
    }

    private void readPatient() {
        System.out.println("-----");
        System.out.println("Read Patient");

        // Collect doctor information with validation
        int id = getIntInput("Enter MRN: ");

        // Attempt to read patient from database
        try {
            Patients patient = patientDAO.readPatient(id);
            if (patient != null) {
                System.out.println(patient.toString());
            } else {
                System.out.println("Patient with MRN " + id + " not found");
            }
        } catch (Exception e) {
            // Handle database errors (e.g., duplicate doctor ID)
            System.out.println("Error reading patient: " + e.getMessage());
        }
        System.out.println();
    }

    private void readAllPatients() {
        System.out.println("-----");
        System.out.println("Read All Patients");

        // Attempt to read all patients from the database
        try {
            List<Patients> patients = patientDAO.readAllPatients();
            if (!patients.isEmpty()) {
                for (Patients p : patients) {
                    System.out.println(p.toString());
                }
            } else {
                System.out.println("No patients found");
            }
        } catch (Exception e) {
            // Handle database errors (e.g., duplicate doctor ID)
            System.out.println("Error reading all Patients: " + e.getMessage());
        }
        System.out.println();
    }

    private void updatePatient() {
        System.out.println("-----");
        System.out.println("Update Patient");

        int mrn = getIntInput("Enter Patient MRN: ");
        Patients patient;
        try {
            patient = patientDAO.readPatient(mrn);
        } catch (SQLException e) {
            System.out.println("Error fetching patient: " + e.getMessage());
            return;
        }

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
        System.out.println();
    }

    private void deletePatient() {
        System.out.println("\n--- Delete Patient (WIP) ---");
        int mrn = getIntInput("Enter MRN: ");

        try {
            if (patientDAO.deletePatient(mrn)) {
                System.out.println("\nPatient deleted successfully!");
            } else {
                System.out.println("\n!!! Failed to delete patient !!!");
            }
        } catch (Exception e) {
            // Handle database errors (e.g., duplicate MRN, connection issues)
            System.out.println("\n!!! Error deleting patient: " + e.getMessage() + " !!!");
        }

          
    }
}
