package main.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import main.models.Doctors;
import main.util.Database;

/**
 * Data Access Object (DAO) for managing doctor records in the database.
 * This class provides methods to perform create operations on the doctors
 * table.
 *
 * The DAO pattern is used to separate business logic from data access logic,
 * providing a clean interface for database operations related to doctors.
 */
public class DoctorDAO {

    /** Database connection utility instance */
    private Database db;

    /**
     * Constructs a new DoctorDAO with the specified database connection.
     *
     * @param db the Database utility instance for managing connections
     */
    public DoctorDAO(Database db) {
        this.db = db;
    }

    /**
     * Creates a new doctor record in the database.
     * This method inserts a new doctor into the doctors table with all required
     * information.
     *
     * @param doctor the Doctors object containing all doctor information to be
     *               inserted
     * @return true if the doctor was successfully created, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean createDoctor(Doctors doctor) throws SQLException {
        // SQL INSERT statement for creating a new doctor record
        String sql = "INSERT INTO doctors (id, name) VALUES (?, ?)";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            // Set parameters in the prepared statement to prevent SQL injection
            stmt.setString(1, doctor.getId());
            stmt.setString(2, doctor.getName());

            // Execute the insert and return success status
            return stmt.executeUpdate() > 0;
        }
    }

    public Doctors getDoctorID(String id) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new Doctors(rs.getString("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Fatal Error" + e.getMessage());
        }
        return null;
    }

    public boolean updateDoctor(Doctors doctor) {
        String sql = "UPDATE doctors SET name = ? WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error occurred" + e.getMessage());
            return false;
        }
    }
}
