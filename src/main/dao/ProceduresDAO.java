package main.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.models.Procedures;
import main.util.Database;

/**
 * Data Access Object (DAO) for managing medical procedures in the database.
 * This class provides methods to perform CRUD operations on the procedures
 * table.
 *
 * Medical procedures define the available treatments and services that can be
 * performed on patients.
 */
public class ProceduresDAO {

    /** Database connection utility instance */
    private Database db;

    /**
     * Constructs a new ProceduresDAO with the specified database connection.
     *
     * @param db the Database utility instance for managing connections
     */
    public ProceduresDAO(Database db) {
        this.db = db;
    }

    /**
     * Creates a new medical procedure record in the database.
     * This method inserts a new procedure definition into the procedures table
     * with all required information including description, duration, and doctor ID.
     *
     * @param procedure the Procedures object containing the procedure information
     *                  to be inserted
     * @return true if the procedure was successfully created, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean createProcedure(Procedures procedure) throws SQLException {
        // SQL INSERT statement for creating a new procedure record
        String sql =
            "INSERT INTO procedures (id, name, description, duration, doctorId) VALUES (?, ?, ?, ?, ?)";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            // Set parameters in the prepared statement to prevent SQL injection
            stmt.setString(1, procedure.getId());
            stmt.setString(2, procedure.getName());
            stmt.setString(3, procedure.getDescription());
            stmt.setInt(4, procedure.getDuration());
            stmt.setString(5, procedure.getDoctorId());

            // Execute the insert and return success status
            return stmt.executeUpdate() > 0;
        }
    }

    public Procedures getProcedureID(String id) {
        String sql = "SELECT * FROM procedures WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Procedures(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("duration"),
                    rs.getString("doctorId")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error occured" + e.getMessage());
        }
        return null;
    }

    public boolean updateProcedure(Procedures procedure) throws SQLException {
        String sql =
            "UPDATE procedures SET name = ?, description = ?, duration = ?, doctorId = ? WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, procedure.getName());
            stmt.setString(2, procedure.getDescription());
            stmt.setInt(3, procedure.getDuration());
            stmt.setString(4, procedure.getDoctorId());
            stmt.setString(5, procedure.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error occurred" + e.getMessage());
            return false;
        }
    }
}
