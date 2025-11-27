package main.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Procedures;
import main.util.Database;

public class ProceduresDAO {

    private Database db;

    public ProceduresDAO(Database db) {
        this.db = db;
    }

    public boolean createProcedure(Procedures procedure) throws SQLException {
        String sql =
            "INSERT INTO procedures (id, name, description, duration, doctorId) VALUES (?, ?, ?, ?, ?)";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, procedure.getId());
            stmt.setString(2, procedure.getName());
            stmt.setString(3, procedure.getDescription());
            stmt.setInt(4, procedure.getDuration());
            stmt.setString(5, procedure.getDoctorId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteProcedure(String id) throws SQLException {
        String sql = "DELETE FROM procedures WHERE id = ?";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    public Procedures readProcedure(String id) throws SQLException {
        String sql = "SELECT * FROM procedures WHERE id = ?";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            var queryResult = stmt.executeQuery();
            if (queryResult.next()) {
                Procedures procedure = new Procedures(
                    queryResult.getString("id"),
                    queryResult.getString("name"),
                    queryResult.getString("description"),
                    queryResult.getInt("duration"),
                    queryResult.getString("doctorId")
                );
                return procedure;
            } else {
                return null;
            }
        }
    }

    public List<Procedures> readAllProcedures() throws SQLException {
        String sql = "SELECT * FROM procedures";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            ResultSet queryResult = stmt.executeQuery();
            List<Procedures> procedures = new ArrayList<>();
            while (queryResult.next()) {
                Procedures procedure = new Procedures(
                    queryResult.getString("id"),
                    queryResult.getString("name"),
                    queryResult.getString("description"),
                    queryResult.getInt("duration"),
                    queryResult.getString("doctorId")
                );
                procedures.add(procedure);
            }
            return procedures;
        }
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
        }
    }
}
