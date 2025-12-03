package main.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import main.exception.DatabaseException;
import main.model.Patient;
import main.util.Database;

/**
 * Data Access Object for Patient entities.
 * <p>
 * This class provides CRUD operations for the patients table in the database.
 * It handles the conversion between Java objects and database records,
 * including date format handling for legacy data.
 * </p>
 *
 */
public class PatientDAO implements BaseDAO<Patient, Integer> {

    private static final DateTimeFormatter DOB_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LEGACY_DOB_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter US_DOB_FORMATTER =
        DateTimeFormatter.ofPattern("M/d/yyyy");

    private final Database db;

    /**
     * Constructs a new PatientDAO with the specified database connection.
     *
     * @param db the database connection to use
     */
    public PatientDAO(Database db) {
        this.db = db;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean create(Patient patient) throws DatabaseException {
        // Define the SQL insert statement for the patients table
        String sql =
            "INSERT INTO patients (mrn, fname, lname, dob, address, state, city, zip, insurance, email) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Prepare the prepared statement with the database connection
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            // Set the parameters for the prepared statement from the patient object
            stmt.setInt(1, patient.getMrn());
            stmt.setString(2, patient.getFname());
            stmt.setString(3, patient.getLname());
            stmt.setString(4, patient.getDob().format(DOB_FORMATTER));
            stmt.setString(5, patient.getAddress());
            stmt.setString(6, patient.getState());
            stmt.setString(7, patient.getCity());
            stmt.setInt(8, patient.getZip());
            stmt.setString(9, patient.getInsurance());
            stmt.setString(10, patient.getEmail());

            // Execute the insert and return true if at least one row was affected
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Handle SQL exceptions by throwing a custom database exception
            throw new DatabaseException(
                "Failed to create patient: " + e.getMessage(),
                e
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Patient read(Integer mrn) throws DatabaseException {
        // Define the SQL select statement to retrieve a patient by MRN
        String sql = "SELECT * FROM patients WHERE mrn = ?";

        try (
            // Prepare the prepared statement with the database connection
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            // Set the MRN parameter in the prepared statement
            stmt.setInt(1, mrn);
            try (ResultSet resultSet = stmt.executeQuery()) {
                // Check if a result was found
                if (resultSet.next()) {
                    // Map the result set to a Patient object and return it
                    return mapResultSetToPatient(resultSet);
                }
                // Return null if no patient was found
                return null;
            }
        } catch (SQLException e) {
            // Handle SQL exceptions by throwing a custom database exception
            throw new DatabaseException(
                "Failed to read patient: " + e.getMessage(),
                e
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Patient> readAll() throws DatabaseException {
        String sql = "SELECT * FROM patients";
        List<Patient> patients = new ArrayList<>();

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery()
        ) {
            while (resultSet.next()) {
                patients.add(mapResultSetToPatient(resultSet));
            }
            return patients;
        } catch (SQLException e) {
            throw new DatabaseException(
                "Failed to read all patients: " + e.getMessage(),
                e
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(Patient patient) throws DatabaseException {
        String sql =
            "UPDATE patients SET fname = ?, lname = ?, dob = ?, address = ?, city = ?, " +
            "state = ?, zip = ?, insurance = ?, email = ? WHERE mrn = ?";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, patient.getFname());
            stmt.setString(2, patient.getLname());
            stmt.setString(3, patient.getDob().format(DOB_FORMATTER));
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getCity());
            stmt.setString(6, patient.getState());
            stmt.setInt(7, patient.getZip());
            stmt.setString(8, patient.getInsurance());
            stmt.setString(9, patient.getEmail());
            stmt.setInt(10, patient.getMrn());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException(
                "Failed to update patient: " + e.getMessage(),
                e
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Integer mrn) throws DatabaseException {
        String sql = "DELETE FROM patients WHERE mrn = ?";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setInt(1, mrn);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException(
                "Failed to delete patient: " + e.getMessage(),
                e
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(Integer mrn) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM patients WHERE mrn = ?";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setInt(1, mrn);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new DatabaseException(
                "Failed to check patient existence: " + e.getMessage(),
                e
            );
        }
    }

    // ========== Helper Methods ==========

    /**
     * Maps a ResultSet row to a Patient object.
     *
     * @param resultSet the ResultSet positioned at a valid row
     * @return a Patient object populated with data from the ResultSet
     * @throws SQLException if a database access error occurs
     */
    private Patient mapResultSetToPatient(ResultSet resultSet)
        throws SQLException {
        // Extract the date of birth string from the result set
        String dobString = resultSet.getString("dob");
        // Parse the DOB string into a LocalDate object using the helper method
        LocalDate dob = parseDob(dobString);

        // Create and return a new Patient object populated with all fields from the result set
        return new Patient(
            resultSet.getInt("mrn"),
            resultSet.getString("fname"),
            resultSet.getString("lname"),
            dob,
            resultSet.getString("address"),
            resultSet.getString("state"),
            resultSet.getString("city"),
            resultSet.getInt("zip"),
            resultSet.getString("insurance"),
            resultSet.getString("email")
        );
    }

    /**
     * Parses a date of birth string, supporting multiple formats.
     * <p>
     * This method attempts to parse using the following formats in order:
     * 1. Standard yyyy-MM-dd format
     * 2. US MM/dd/yyyy format
     * 3. Legacy dd/MM/yyyy format
     * </p>
     *
     * @param dobString the date string to parse
     * @return the parsed LocalDate
     * @throws DateTimeParseException if the date cannot be parsed with any format
     */
    private LocalDate parseDob(String dobString) {
        try {
            // Attempt to parse the DOB using the standard yyyy-MM-dd format
            return LocalDate.parse(dobString, DOB_FORMATTER);
        } catch (DateTimeParseException e1) {
            // If standard format fails, try the US MM/dd/yyyy format
            try {
                return LocalDate.parse(dobString, US_DOB_FORMATTER);
            } catch (DateTimeParseException e2) {
                // If US format fails, try the legacy dd/MM/yyyy format
                return LocalDate.parse(dobString, LEGACY_DOB_FORMATTER);
            }
        }
    }
}
