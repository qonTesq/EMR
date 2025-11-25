package main.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        String sql =
            "INSERT INTO patients (mrn, fname, lname, dob, address, state, city, zip, insurance, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            // Set parameters in the prepared statement to prevent SQL injection
            stmt.setInt(1, patient.getMrn());
            stmt.setString(2, patient.getFname());
            stmt.setString(3, patient.getLname());
            // dob is stored as text in database
            stmt.setString(
                4,
                patient
                    .getDob()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            );
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

    /**
     * Retrieves a patient record by their MRN (Medical Record Number).
     * This method queries the patients table for a specific patient by their unique MRN.
     *
     * @param mrn the MRN of the patient to retrieve
     * @return the Patients object if found, null otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public Patients getPatient(int mrn) throws SQLException {
        String sql = "SELECT * FROM patients WHERE mrn = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setInt(1, mrn);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                // dob is stored as text in database
                String dobString = rs.getString("dob");
                java.time.LocalDate dob = java.time.LocalDate.parse(
                    dobString,
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
                );

                return new Patients(
                    rs.getInt("mrn"),
                    rs.getString("fname"),
                    rs.getString("lname"),
                    dob,
                    rs.getString("address"),
                    rs.getString("state"),
                    rs.getString("city"),
                    rs.getInt("zip"),
                    rs.getString("insurance"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            // Silently handle error - not finding a patient is not exceptional
        }
        return null;
    }

    /**
     * Retrieves all patient records from the database.
     * This method returns a list of all patients stored in the patients table.
     *
     * @return a list of all Patients objects, or an empty list if no patients exist
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public List<Patients> getAllPatients() throws SQLException {
        // TODO: Implement read all logic
        // Use SELECT * SQL statement to retrieve all patients
        // Return a list of Patients objects
        throw new UnsupportedOperationException(
            "getAllPatients not yet implemented"
        );
    }

    /**
     * Updates an existing patient record in the database.
     * This method updates all fields of a patient record identified by their MRN.
     *
     * @param patient the Patients object containing updated information
     * @return true if the patient was successfully updated, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean updatePatient(Patients patient) throws SQLException {
        String sql =
            "UPDATE patients SET fname = ?, lname = ?, dob = ?, address = ?, city = ?, state =?, zip =?, insurance = ?, email =? WHERE mrn = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, patient.getFname());
            stmt.setString(2, patient.getLname());
            // dob is stored as text in database
            stmt.setString(
                3,
                patient
                    .getDob()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            );
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getCity());
            stmt.setString(6, patient.getState());
            stmt.setInt(7, patient.getZip());
            stmt.setString(8, patient.getInsurance());
            stmt.setString(9, patient.getEmail());
            stmt.setInt(10, patient.getMrn());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Silently handle error - update may fail due to invalid data
            return false;
        }
    }

    /**
     * Deletes a patient record from the database.
     * This method removes a patient from the patients table by their MRN.
     *
     * @param mrn the MRN of the patient to delete
     * @return true if the patient was successfully deleted, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    
}
