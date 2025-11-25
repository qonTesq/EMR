package main.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
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

    /**
     * Retrieves a procedure record by its ID.
     * This method queries the procedures table for a specific procedure by its unique ID.
     *
     * @param id the ID of the procedure to retrieve
     * @return the Procedures object if found, null otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public Procedures getProcedure(String id) throws SQLException {
        String sql = "SELECT * FROM procedures WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            var rs = stmt.executeQuery();

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
            // Silently handle error - not finding a procedure is not exceptional
        }
        return null;
    }

    /**
     * Retrieves all procedure records from the database.
     * This method returns a list of all procedures stored in the procedures table.
     *
     * @return a list of all Procedures objects, or an empty list if no procedures exist
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public List<Procedures> getAllProcedures() throws SQLException {
        // TODO: Implement read all logic
        // Use SELECT * SQL statement to retrieve all procedures
        // Return a list of Procedures objects
        throw new UnsupportedOperationException(
            "getAllProcedures not yet implemented"
        );
    }

    /**
     * Updates an existing procedure record in the database.
     * This method updates all fields of a procedure record identified by its ID.
     *
     * @param procedure the Procedures object containing updated information
     * @return true if the procedure was successfully updated, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
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
            // Silently handle error - update may fail due to invalid data
            return false;
        }
    }

    /**
     * Deletes a procedure record from the database.
     * This method removes a procedure from the procedures table by its ID.
     *
     * @param id the ID of the procedure to delete
     * @return true if the procedure was successfully deleted, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean deleteProcedure(String id) throws SQLException {
        // TODO: Implement deletion logic
        // Use DELETE SQL statement with procedure ID parameter
        throw new UnsupportedOperationException(
            "deleteProcedure not yet implemented"
        );
    }
}
