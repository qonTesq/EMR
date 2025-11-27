package main.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Doctors;
import main.util.Database;

public class DoctorDAO {

    private Database db;

    public DoctorDAO(Database db) {
        this.db = db;
    }

    public boolean createDoctor(Doctors doctor) throws SQLException {
        String sql = "INSERT INTO doctors (id, name) VALUES (?, ?)";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, doctor.getId());
            stmt.setString(2, doctor.getName());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteDoctor(String doctorId) throws SQLException {
    String sql = "DELETE FROM doctors WHERE id = ?";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, doctorId);

            // executeUpdate() returns number of rows affected
            return stmt.executeUpdate() > 0;
        }
    }

    public Doctors getDoctor(String id) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE id = ?";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            var queryResult = stmt.executeQuery();
            if (queryResult.next()) {
                Doctors doctor = new Doctors(
                    queryResult.getString("id"),
                    queryResult.getString("name")
                );
                return doctor;
            } else {
                return null;
            }
        }
    }

    public List<Doctors> readAllDoctors() throws SQLException {
        String sql = "SELECT * FROM doctors";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            ResultSet queryResult = stmt.executeQuery();
            List<Doctors> doctors = new ArrayList<>();
            while (queryResult.next()) {
                Doctors doctor = new Doctors(
                    queryResult.getString("id"),
                    queryResult.getString("name")
                );
                doctors.add(doctor);
            }
            return doctors;
        }
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
            return false;
        }
    }
}
