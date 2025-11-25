package main.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import main.models.Doctors;
import main.util.Database;

/**
 * Data Access Object (DAO) for managing doctor records in the database.
 * This class provides methods to perform CRUD operations on the doctors
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

    public Doctors readDoctor(String id) throws SQLException {
        // SQL select statement for creating a new procedure record
        String sql = "SELECT * FROM doctors WHERE id = ?";

        try(PreparedStatement stmt = db.getConnection().prepareStatement(sql))
        {
            stmt.setString(1, id);
            var queryResult = stmt.executeQuery();
            if(queryResult.next())
                {
                 Doctors doctor = new Doctors(
                queryResult.getString("id"),
                queryResult.getString("name")
                );
                return doctor;
            } 
            else 
            {
                return null;
            }
        }
    }

    public ArrayList<Doctors> readAllDoctors() throws SQLException {
       
        // SQL select statement for creating a new procedure record
        String sql = "SELECT * FROM doctors";

        try(PreparedStatement stmt = db.getConnection().prepareStatement(sql))
        {
            ResultSet queryResult = stmt.executeQuery();
            
            ArrayList<Doctors> doctors = new ArrayList<>();

            while(queryResult.next()) {
                Doctors doctor = new Doctors(
                queryResult.getString("id"),
                queryResult.getString("name")
                );
                doctors.add(doctor);
            } 
            
            return doctors;
            
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
            return new ArrayList<>(); 
        }
    }
    

    /**
     * Retrieves a doctor record by their ID.
     * This method queries the doctors table for a specific doctor by their unique ID.
     *
     * @param id the ID of the doctor to retrieve
     * @return the Doctors object if found, null otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public Doctors getDoctor(String id) throws SQLException {
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
            // Silently handle error - not finding a doctor is not exceptional
        }
        return null;
    }

    /**
     * Retrieves all doctor records from the database.
     * This method returns a list of all doctors stored in the doctors table.
     *
     * @return a list of all Doctors objects, or an empty list if no doctors exist
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public List<Doctors> getAllDoctors() throws SQLException {
        // TODO: Implement read all logic
        // Use SELECT * SQL statement to retrieve all doctors
        // Return a list of Doctors objects
        throw new UnsupportedOperationException(
            "getAllDoctors not yet implemented"
        );
    }

    /**
     * Updates an existing doctor record in the database.
     * This method updates all fields of a doctor record identified by their ID.
     *
     * @param doctor the Doctors object containing updated information
     * @return true if the doctor was successfully updated, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean updateDoctor(Doctors doctor) {
        String sql = "UPDATE doctors SET name = ? WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Silently handle error - update may fail due to invalid data
            return false;
        }
    }

    /**
     * Deletes a doctor record from the database.
     * This method removes a doctor from the doctors table by their ID.
     *
     * @param id the ID of the doctor to delete
     * @return true if the doctor was successfully deleted, false otherwise
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      fails
     */
    public boolean deleteDoctor(String id) throws SQLException {
        // TODO: Implement deletion logic
        // Use DELETE SQL statement with doctor ID parameter
        throw new UnsupportedOperationException(
            "deleteDoctor not yet implemented"
        );
    }
}
