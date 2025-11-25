package main.dao;

import java.sql.*;
import java.time.LocalDate;
import main.models.PatientHistory;
import main.util.Database;

/**
 * Data Access Object (DAO) for managing patient history records in the
 * database.
 * This class provides methods to perform CRUD operations on the
 * patient_history table.
 *
 * Patient history records link patients to medical procedures, including
 * billing information and attending doctors.
 */
public class PatientHistoryDAO {

    /** Database connection utility instance */
    private Database db;

    /**
     * Constructs a new PatientHistoryDAO with the specified database connection.
     *
     * @param db the Database utility instance for managing connections
     */
    public PatientHistoryDAO(Database db) {
        this.db = db;
    }

    /**
     * Creates a new patient history record in the database.
     * This method inserts a new record into the patient_history table that links
     * a patient to a medical procedure with associated billing and doctor
     * information.
     *
     * @param patientHistory the PatientHistory object containing all record
     *                       information to be inserted
     * @return true if the patient history record was successfully created, false
     *         otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean createPatientHistory(PatientHistory patientHistory)
        throws SQLException {
        // SQL INSERT statement for creating a new patient history record
        String sql =
            "INSERT INTO patient_history (id, patientId, procedureId, date, billing, doctorId) VALUES (?, ?, ?, ?, ?, ?)";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            // Set parameters in the prepared statement to prevent SQL injection
            stmt.setString(1, patientHistory.getId());
            stmt.setInt(2, patientHistory.getPatientId());
            stmt.setString(3, patientHistory.getProcedureId());
            stmt.setDate(4, Date.valueOf(patientHistory.getDate()));
            stmt.setDouble(5, patientHistory.getBilling());
            stmt.setString(6, patientHistory.getDoctorId());

            // Execute the insert and return success status
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves a patient history record by its ID.
     * This method queries the patient_history table for a specific record by its unique ID.
     *
     * @param id the ID of the patient history record to retrieve
     * @return the PatientHistory object if found, null otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public PatientHistory getPatientHistory(String id) throws SQLException {
        String sql = "SELECT * FROM patient_history WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // date is stored as DATE in database
                java.sql.Date sqlDate = rs.getDate("date");
                LocalDate date = sqlDate.toLocalDate();

                return new PatientHistory(
                    rs.getString("id"),
                    rs.getInt("patientId"),
                    rs.getString("procedureId"),
                    date,
                    rs.getDouble("billing"),
                    rs.getString("doctorId")
                );
            }
        } catch (SQLException e) {
            // Silently handle error - not finding a patient history is not exceptional
        }
        return null;
    }

    /**
     * Retrieves all patient history records from the database.
     * This method returns a list of all patient history records stored in the patient_history table.
     *
     * @return a list of all PatientHistory objects, or an empty list if no records exist
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public java.util.List<PatientHistory> getAllPatientHistory()
        throws SQLException {
        // TODO: Implement read all logic
        // Use SELECT * SQL statement to retrieve all patient history records
        // Return a list of PatientHistory objects
        throw new UnsupportedOperationException(
            "getAllPatientHistory not yet implemented"
        );
    }

    /**
     * Updates an existing patient history record in the database.
     * This method updates all fields of a patient history record identified by its ID.
     *
     * @param patientHistory the PatientHistory object containing updated information
     * @return true if the record was successfully updated, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean updatePatientHistory(PatientHistory patientHistory)
        throws SQLException {
        String sql =
            "UPDATE patient_history SET id = ?, patientId = ?, procedureId = ?, date = ?, billing = ?, doctorId = ? WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, patientHistory.getId());
            stmt.setInt(2, patientHistory.getPatientId());
            stmt.setString(3, patientHistory.getProcedureId());
            stmt.setDate(4, Date.valueOf(patientHistory.getDate()));
            stmt.setDouble(5, patientHistory.getBilling());
            stmt.setString(6, patientHistory.getDoctorId());
            stmt.setString(7, patientHistory.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Silently handle error - update may fail due to invalid data
            return false;
        }
    }

    /**
     * Deletes a patient history record from the database.
     * This method removes a patient history record from the patient_history table by its ID.
     *
     * @param id the ID of the patient history record to delete
     * @return true if the record was successfully deleted, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean deletePatientHistory(String id) throws SQLException {
        // TODO: Implement deletion logic
        // Use DELETE SQL statement with patient history ID parameter
        throw new UnsupportedOperationException(
            "deletePatientHistory not yet implemented"
        );
    }
}
