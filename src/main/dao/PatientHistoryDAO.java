package main.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.PatientHistory;
import main.util.Database;

public class PatientHistoryDAO {

    private Database db;

    public PatientHistoryDAO(Database db) {
        this.db = db;
    }

    public boolean createPatientHistory(PatientHistory patientHistory)
        throws SQLException {
        String sql =
            "INSERT INTO patient_history (id, patientId, procedureId, date, billing, doctorId) VALUES (?, ?, ?, ?, ?, ?)";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, patientHistory.getId());
            stmt.setInt(2, patientHistory.getPatientId());
            stmt.setString(3, patientHistory.getProcedureId());
            stmt.setDate(4, Date.valueOf(patientHistory.getDate()));
            stmt.setDouble(5, patientHistory.getBilling());
            stmt.setString(6, patientHistory.getDoctorId());

            return stmt.executeUpdate() > 0;
        }
    }
    public boolean deletePatientHistory(String historyId) throws SQLException {
    String sql = "DELETE FROM patient_history WHERE id = ?";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, historyId);

            // executeUpdate() returns the number of rows affected
            return stmt.executeUpdate() > 0;
        }
    }

    public PatientHistory readPatientHistory(String id) throws SQLException {
        String sql = "SELECT * FROM patient_history WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            ResultSet queryResult = stmt.executeQuery();
            if (queryResult.next()) {
                PatientHistory patientHistory = new PatientHistory(
                    queryResult.getString("id"),
                    queryResult.getInt("patientId"),
                    queryResult.getString("procedureId"),
                    queryResult.getDate("date").toLocalDate(),
                    queryResult.getDouble("billing"),
                    queryResult.getString("doctorId")
                );
                return patientHistory;
            } else {
                return null;
            }
        }
    }

    public List<PatientHistory> readAllPatientHistories() throws SQLException {
        String sql = "SELECT * FROM patient_history";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            ResultSet queryResult = stmt.executeQuery();
            List<PatientHistory> patientHistories = new ArrayList<>();
            while (queryResult.next()) {
                PatientHistory patientHistory = new PatientHistory(
                    queryResult.getString("id"),
                    queryResult.getInt("patientId"),
                    queryResult.getString("procedureId"),
                    queryResult.getDate("date").toLocalDate(),
                    queryResult.getDouble("billing"),
                    queryResult.getString("doctorId")
                );
                patientHistories.add(patientHistory);
            }
            return patientHistories;
        }
    }

    public boolean updatePatientHistory(PatientHistory patientHistory)
        throws SQLException {
        String sql =
            "UPDATE patient_history SET patientId = ?, procedureId = ?, date = ?, billing = ?, doctorId = ? WHERE id = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setInt(1, patientHistory.getPatientId());
            stmt.setString(2, patientHistory.getProcedureId());
            stmt.setDate(3, Date.valueOf(patientHistory.getDate()));
            stmt.setDouble(4, patientHistory.getBilling());
            stmt.setString(5, patientHistory.getDoctorId());
            stmt.setString(6, patientHistory.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
