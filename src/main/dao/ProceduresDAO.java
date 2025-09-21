package main.dao;

import java.sql.PreparedStatement;
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
     * with a unique ID and descriptive name.
     *
     * @param procedure the Procedures object containing the procedure information
     *                  to be inserted
     * @return true if the procedure was successfully created, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean createProcedure(Procedures procedure) throws SQLException {
        // SQL INSERT statement for creating a new procedure record
        String sql = "INSERT INTO procedures (id, name) VALUES (?, ?)";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            // Set parameters in the prepared statement to prevent SQL injection
            stmt.setString(1, procedure.getId());
            stmt.setString(2, procedure.getName());

            // Execute the insert and return success status
            return stmt.executeUpdate() > 0;
        }
    }
}