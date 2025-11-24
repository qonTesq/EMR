package test.main.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import main.dao.DoctorDAO;
import main.dao.PatientDAO;
import main.dao.PatientHistoryDAO;
import main.dao.ProceduresDAO;
import main.models.Doctors;
import main.models.PatientHistory;
import main.models.Patients;
import main.models.Procedures;
import main.util.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for PatientHistoryDAO update operations.
 * Tests the updatePatientHistory method to ensure it correctly updates patient history records in the database.
 */
public class PatientHistoryDAOTest {

    private Database db;
    private PatientHistoryDAO patientHistoryDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize test database connection
        db = new Database();
        patientHistoryDAO = new PatientHistoryDAO(db);

        // Create test dependencies (doctors, procedures, patients)
        createTestDependencies();

        // Ensure test table is clean
        cleanTestData();
    }

    private void createTestDependencies() throws SQLException {
        DoctorDAO doctorDAO = new DoctorDAO(db);
        ProceduresDAO proceduresDAO = new ProceduresDAO(db);
        PatientDAO patientDAO = new PatientDAO(db);

        // Create test doctors
        try {
            doctorDAO.createDoctor(new Doctors("DR-001", "Dr. Test One"));
        } catch (SQLException e) {
            // Doctor might already exist, ignore
        }
        try {
            doctorDAO.createDoctor(new Doctors("DR-002", "Dr. Test Two"));
        } catch (SQLException e) {
            // Doctor might already exist, ignore
        }

        // Create test procedures
        try {
            proceduresDAO.createProcedure(
                new Procedures(
                    "PROC-001",
                    "Test Procedure 1",
                    "Description",
                    30,
                    "DR-001"
                )
            );
        } catch (SQLException e) {
            // Procedure might already exist, ignore
        }
        try {
            proceduresDAO.createProcedure(
                new Procedures(
                    "PROC-002",
                    "Test Procedure 2",
                    "Description",
                    45,
                    "DR-002"
                )
            );
        } catch (SQLException e) {
            // Procedure might already exist, ignore
        }

        // Create test patients
        try {
            patientDAO.createPatient(
                new Patients(
                    12345,
                    "Test",
                    "Patient1",
                    LocalDate.of(1990, 1, 1),
                    "123 Main St",
                    "NY",
                    "New York",
                    10001,
                    "Insurance",
                    "test1@example.com"
                )
            );
        } catch (SQLException e) {
            // Patient might already exist, ignore
        }
        try {
            patientDAO.createPatient(
                new Patients(
                    12346,
                    "Test",
                    "Patient2",
                    LocalDate.of(1991, 2, 2),
                    "456 Oak Ave",
                    "CA",
                    "Los Angeles",
                    90210,
                    "Insurance",
                    "test2@example.com"
                )
            );
        } catch (SQLException e) {
            // Patient might already exist, ignore
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        cleanTestData();
        if (db != null) {
            db.close();
        }
    }

    private void cleanTestData() throws SQLException {
        // Clean up test data - remove test patient history if exist
        try (
            var stmt = db
                .getConnection()
                .prepareStatement(
                    "DELETE FROM patient_history WHERE id = ? OR id = ? OR id = ?"
                )
        ) {
            stmt.setString(1, "TEST-001");
            stmt.setString(2, "TEST-002");
            stmt.setString(3, "TEST-003");
            stmt.executeUpdate();
        }
    }

    @Test
    void testUpdatePatientHistory_Success() throws SQLException {
        // Arrange: Create and insert a test patient history record
        PatientHistory originalHistory = new PatientHistory(
            "TEST-001",
            12345,
            "PROC-001",
            LocalDate.of(2023, 5, 15),
            1500.00,
            "DR-001"
        );
        patientHistoryDAO.createPatientHistory(originalHistory);

        // Act: Update patient history information
        PatientHistory updatedHistory = new PatientHistory(
            "TEST-001",
            12346,
            "PROC-002",
            LocalDate.of(2023, 6, 16),
            2000.00,
            "DR-002"
        );
        boolean result = patientHistoryDAO.updatePatientHistory(updatedHistory);

        // Assert: Verify update was successful
        assertTrue(result, "Update should return true on success");

        // Verify the changes were persisted
        PatientHistory retrieved = patientHistoryDAO.getPatientHistoryID(
            "TEST-001"
        );
        assertNotNull(retrieved, "Patient history should exist after update");
        assertEquals(12346, retrieved.getPatientId());
        assertEquals("PROC-002", retrieved.getProcedureId());
        assertEquals(LocalDate.of(2023, 6, 16), retrieved.getDate());
        assertEquals(2000.00, retrieved.getBilling());
        assertEquals("DR-002", retrieved.getDoctorId());
    }

    @Test
    void testUpdatePatientHistory_PartialUpdate() throws SQLException {
        // Arrange: Create and insert a test patient history record
        PatientHistory originalHistory = new PatientHistory(
            "TEST-001",
            12345,
            "PROC-001",
            LocalDate.of(2023, 5, 15),
            1500.00,
            "DR-001"
        );
        patientHistoryDAO.createPatientHistory(originalHistory);

        // Act: Update only some fields
        PatientHistory partialUpdate = new PatientHistory(
            "TEST-001",
            12345,
            "PROC-001",
            LocalDate.of(2023, 5, 15),
            1500.00,
            "DR-001"
        );
        partialUpdate.setBilling(1750.00); // Only change billing
        boolean result = patientHistoryDAO.updatePatientHistory(partialUpdate);

        // Assert: Verify update was successful and only specified field changed
        assertTrue(result, "Partial update should return true");
        PatientHistory retrieved = patientHistoryDAO.getPatientHistoryID(
            "TEST-001"
        );
        assertEquals(1750.00, retrieved.getBilling());
        assertEquals(12345, retrieved.getPatientId()); // Unchanged
    }

    @Test
    void testUpdatePatientHistory_NonExistentRecord() throws SQLException {
        // Arrange: Try to update a record that doesn't exist
        PatientHistory nonExistentHistory = new PatientHistory(
            "TEST-001",
            12345,
            "PROC-001",
            LocalDate.of(2023, 5, 15),
            1500.00,
            "DR-001"
        );

        // Act: Attempt update
        boolean result = patientHistoryDAO.updatePatientHistory(
            nonExistentHistory
        );

        // Assert: Update should fail for non-existent record
        assertFalse(
            result,
            "Update should return false for non-existent record"
        );
    }

    @Test
    void testUpdatePatientHistory_InvalidData() throws SQLException {
        // Arrange: Create and insert a test patient history record
        PatientHistory originalHistory = new PatientHistory(
            "TEST-001",
            12345,
            "PROC-001",
            LocalDate.of(2023, 5, 15),
            1500.00,
            "DR-001"
        );
        patientHistoryDAO.createPatientHistory(originalHistory);

        // Act: Update with invalid data (invalid patientId)
        PatientHistory invalidHistory = new PatientHistory(
            "TEST-001",
            -1,
            "PROC-001",
            LocalDate.of(2023, 5, 15),
            1500.00,
            "DR-001"
        );

        // Assert: Update with invalid data should return false (caught internally)
        boolean result = patientHistoryDAO.updatePatientHistory(invalidHistory);
        assertFalse(
            result,
            "Update with invalid patientId should return false"
        );
    }

    @Test
    void testCreatePatientHistory_Success() throws SQLException {
        // Note: In a real scenario, you would need to create related Patient, Procedure, and Doctor records first
        // For this test, assuming FK constraints are satisfied or using existing data

        // Arrange: Create a new patient history record
        PatientHistory history = new PatientHistory(
            "TEST-002",
            12345,
            "PROC-001",
            LocalDate.of(2023, 6, 20),
            2000.00,
            "DR-001"
        );

        // Act: Create the patient history
        boolean result = patientHistoryDAO.createPatientHistory(history);

        // Assert: Verify creation was successful
        assertTrue(result, "Create should return true on success");

        // Verify the record was persisted
        PatientHistory retrieved = patientHistoryDAO.getPatientHistoryID(
            "TEST-002"
        );
        assertNotNull(retrieved, "Patient history should exist after creation");
        assertEquals("TEST-002", retrieved.getId());
        assertEquals(12345, retrieved.getPatientId());
        assertEquals("PROC-001", retrieved.getProcedureId());
        assertEquals(LocalDate.of(2023, 6, 20), retrieved.getDate());
        assertEquals(2000.00, retrieved.getBilling());
        assertEquals("DR-001", retrieved.getDoctorId());
    }

    @Test
    void testCreatePatientHistory_DuplicateID() throws SQLException {
        // Arrange: Create and insert a patient history record
        PatientHistory history = new PatientHistory(
            "TEST-003",
            12345,
            "PROC-001",
            LocalDate.of(2023, 7, 10),
            1800.00,
            "DR-001"
        );
        patientHistoryDAO.createPatientHistory(history);

        // Act & Assert: Attempt to create another record with same ID should throw exception
        PatientHistory duplicateHistory = new PatientHistory(
            "TEST-003",
            12346,
            "PROC-002",
            LocalDate.of(2023, 7, 15),
            2200.00,
            "DR-002"
        );

        assertThrows(
            SQLException.class,
            () -> patientHistoryDAO.createPatientHistory(duplicateHistory),
            "Create with duplicate ID should throw SQLException"
        );
    }
}
