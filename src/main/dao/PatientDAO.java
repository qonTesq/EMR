package main.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import main.models.Patients;
import main.util.Database;

public class PatientDAO {

    private static final DateTimeFormatter DOB_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LEGACY_DOB_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Database db;

    public PatientDAO(Database db) {
        this.db = db;
    }

    public boolean createPatient(Patients patient) throws SQLException {
        String sql =
            "INSERT INTO patients (mrn, fname, lname, dob, address, state, city, zip, insurance, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setInt(1, patient.getMrn());
            stmt.setString(2, patient.getFname());
            stmt.setString(3, patient.getLname());

            stmt.setString(
                4,
                patient
                    .getDob()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            );
            stmt.setString(5, patient.getAddress());
            stmt.setString(6, patient.getState());
            stmt.setString(7, patient.getCity());
            stmt.setInt(8, patient.getZip());
            stmt.setString(9, patient.getInsurance());
            stmt.setString(10, patient.getEmail());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletePatient(int mrn) throws SQLException {
        String sql = "DELETE FROM patients WHERE mrn = ?";

        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setInt(1, mrn);

            return stmt.executeUpdate() > 0;
        }
    }

    public Patients readPatient(int mrn) throws SQLException {
        String sql = "SELECT * FROM patients WHERE mrn = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setInt(1, mrn);
            ResultSet queryResult = stmt.executeQuery();
            if (queryResult.next()) {
                String dobString = queryResult.getString("dob");
                LocalDate dob = parseDob(dobString);
                Patients patient = new Patients(
                    queryResult.getInt("mrn"),
                    queryResult.getString("fname"),
                    queryResult.getString("lname"),
                    dob,
                    queryResult.getString("address"),
                    queryResult.getString("state"),
                    queryResult.getString("city"),
                    queryResult.getInt("zip"),
                    queryResult.getString("insurance"),
                    queryResult.getString("email")
                );
                return patient;
            } else {
                return null;
            }
        }
    }

    public List<Patients> readAllPatients() throws SQLException {
        String sql = "SELECT * FROM patients";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            ResultSet queryResult = stmt.executeQuery();
            List<Patients> patients = new ArrayList<>();
            while (queryResult.next()) {
                String dobString = queryResult.getString("dob");
                LocalDate dob = parseDob(dobString);
                Patients patient = new Patients(
                    queryResult.getInt("mrn"),
                    queryResult.getString("fname"),
                    queryResult.getString("lname"),
                    dob,
                    queryResult.getString("address"),
                    queryResult.getString("state"),
                    queryResult.getString("city"),
                    queryResult.getInt("zip"),
                    queryResult.getString("insurance"),
                    queryResult.getString("email")
                );
                patients.add(patient);
            }
            return patients;
        }
    }

    private LocalDate parseDob(String dobString) {
        try {
            return LocalDate.parse(dobString, DOB_FORMATTER);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(dobString, LEGACY_DOB_FORMATTER);
        }
    }

    public boolean updatePatient(Patients patient) throws SQLException {
        String sql =
            "UPDATE patients SET fname = ?, lname = ?, dob = ?, address = ?, city = ?, state =?, zip =?, insurance = ?, email =? WHERE mrn = ?";
        try (
            PreparedStatement stmt = db.getConnection().prepareStatement(sql)
        ) {
            stmt.setString(1, patient.getFname());
            stmt.setString(2, patient.getLname());

            stmt.setString(
                3,
                patient
                    .getDob()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            );
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getCity());
            stmt.setString(6, patient.getState());
            stmt.setInt(7, patient.getZip());
            stmt.setString(8, patient.getInsurance());
            stmt.setString(9, patient.getEmail());
            stmt.setInt(10, patient.getMrn());

            return stmt.executeUpdate() > 0;
        }
    }
}
