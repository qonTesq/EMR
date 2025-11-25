package main.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import main.models.Patients;
import main.util.Database;

/**
 * Data Access Object (DAO) for managing patient records in the database.
 * This class provides methods to perform CRUD operations on the patients
 * table.
 *
 * The DAO pattern is used to separate business logic from data access logic,
 * providing a clean interface for database operations related to patients.
 */
public class PatientDAO {

    /** Database connection utility instance */
    private Database db;

    /**
     * Constructs a new PatientDAO with the specified database connection.
     *
     * @param db the Database utility instance for managing connections
     */
    public PatientDAO(Database db) {
        this.db = db;
    }

    /**
     * Creates a new patient record in the database.
     * This method inserts a new patient into the patients table with all required
     * information.
     *
     * @param patient the Patients object containing all patient information to be
     *                inserted
     * @return true if the patient was successfully created, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean createPatient(Patients patient) throws SQLException {
        // SQL INSERT statement for creating a new patient record
        String sql = "INSERT INTO patients (mrn, fname, lname, dob, address, state, city, zip, insurance, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            // Set parameters in the prepared statement to prevent SQL injection
            stmt.setInt(1, patient.getMrn());
            stmt.setString(2, patient.getFname());
            stmt.setString(3, patient.getLname());
            stmt.setDate(4, Date.valueOf(patient.getDob()));
            stmt.setString(5, patient.getAddress());
            stmt.setString(6, patient.getState());
            stmt.setString(7, patient.getCity());
            stmt.setInt(8, patient.getZip());
            stmt.setString(9, patient.getInsurance());
            stmt.setString(10, patient.getEmail());

            // Execute the insert and return success status
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletePatient(int mrn) throws SQLException {
    String sql = "DELETE FROM patients WHERE mrn = ?";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            
            stmt.setInt(1, mrn);
            

            // executeUpdate() returns number of rows affected
            return stmt.executeUpdate() > 0;


        }

        
    }
}