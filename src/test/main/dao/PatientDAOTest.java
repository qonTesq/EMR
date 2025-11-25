package test.main.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import main.dao.PatientDAO;
import main.models.Patients;
import main.util.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for PatientDAO update operations.
 * Tests the updatePatient method to ensure it correctly updates patient records in the database.
 */
public class PatientDAOTest {

    private Database db;
    private PatientDAO patientDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize test database connection
        // Note: In a real scenario, this would use an in-memory H2 database or test-specific configuration
        db = new Database();
        patientDAO = new PatientDAO(db);

        // Ensure test table is clean
        cleanTestData();
    }

    @AfterEach
    void tearDown() throws SQLException {
        cleanTestData();
        if (db != null) {
            db.close();
        }
    }

    private void cleanTestData() throws SQLException {
        // Clean up test data - remove test patients if exist
        try (
            var stmt = db
                .getConnection()
                .prepareStatement(
                    "DELETE FROM patients WHERE mrn = ? OR mrn = ? OR mrn = ?"
                )
        ) {
            stmt.setInt(1, 999999);
            stmt.setInt(2, 999998);
            stmt.setInt(3, 999997);
            stmt.executeUpdate();
        }
    }

    @Test
    void testUpdatePatient_Success() throws SQLException {
        // Arrange: Create and insert a test patient
        Patients originalPatient = new Patients(
            999999,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "123 Main St",
            "NY",
            "New York",
            10001,
            "BlueCross",
            "john.doe@example.com"
        );
        patientDAO.createPatient(originalPatient);

        // Act: Update patient information
        Patients updatedPatient = new Patients(
            999999,
            "Jane",
            "Smith",
            LocalDate.of(1991, 2, 2),
            "456 Oak Ave",
            "CA",
            "Los Angeles",
            90210,
            "Aetna",
            "jane.smith@example.com"
        );
        boolean result = patientDAO.updatePatient(updatedPatient);

        // Assert: Verify update was successful
        assertTrue(result, "Update should return true on success");

        // Verify the changes were persisted
        Patients retrieved = patientDAO.getPatientMRN(999999);
        assertNotNull(retrieved, "Patient should exist after update");
        assertEquals("Jane", retrieved.getFname());
        assertEquals("Smith", retrieved.getLname());
        assertEquals(LocalDate.of(1991, 2, 2), retrieved.getDob());
        assertEquals("456 Oak Ave", retrieved.getAddress());
        assertEquals("CA", retrieved.getState());
        assertEquals("Los Angeles", retrieved.getCity());
        assertEquals(90210, retrieved.getZip());
        assertEquals("Aetna", retrieved.getInsurance());
        assertEquals("jane.smith@example.com", retrieved.getEmail());
    }

    @Test
    void testUpdatePatient_PartialUpdate() throws SQLException {
        // Arrange: Create and insert a test patient
        Patients originalPatient = new Patients(
            999999,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "123 Main St",
            "NY",
            "New York",
            10001,
            "BlueCross",
            "john.doe@example.com"
        );
        patientDAO.createPatient(originalPatient);

        // Act: Update only some fields
        Patients partialUpdate = new Patients(
            999999,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "123 Main St",
            "NY",
            "New York",
            10001,
            "BlueCross",
            "john.doe@example.com"
        );
        partialUpdate.setFname("Johnny"); // Only change first name
        boolean result = patientDAO.updatePatient(partialUpdate);

        // Assert: Verify update was successful and only specified field changed
        assertTrue(result, "Partial update should return true");
        Patients retrieved = patientDAO.getPatientMRN(999999);
        assertEquals("Johnny", retrieved.getFname());
        assertEquals("Doe", retrieved.getLname()); // Unchanged
    }

    @Test
    void testUpdatePatient_NonExistentPatient() throws SQLException {
        // Arrange: Try to update a patient that doesn't exist
        Patients nonExistentPatient = new Patients(
            999999,
            "Non",
            "Existent",
            LocalDate.of(1990, 1, 1),
            "123 Main St",
            "NY",
            "New York",
            10001,
            "Insurance",
            "non@example.com"
        );

        // Act: Attempt update
        boolean result = patientDAO.updatePatient(nonExistentPatient);

        // Assert: Update should fail for non-existent patient
        assertFalse(
            result,
            "Update should return false for non-existent patient"
        );
    }

    @Test
    void testUpdatePatient_InvalidData() throws SQLException {
        // Arrange: Create and insert a test patient
        Patients originalPatient = new Patients(
            999999,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "123 Main St",
            "NY",
            "New York",
            10001,
            "BlueCross",
            "john.doe@example.com"
        );
        patientDAO.createPatient(originalPatient);

        // Act: Update with invalid data (null fname)
        Patients invalidPatient = new Patients(
            999999,
            null,
            "Doe",
            LocalDate.of(1990, 1, 1),
            "123 Main St",
            "NY",
            "New York",
            10001,
            "BlueCross",
            "john.doe@example.com"
        );

        // Assert: Update with null data should return false (caught internally)
        boolean result = patientDAO.updatePatient(invalidPatient);
        assertFalse(result, "Update with null fname should return false");
    }

    @Test
    void testCreatePatient_Success() throws SQLException {
        // Arrange: Create a new patient
        Patients patient = new Patients(
            999998,
            "Jane",
            "Smith",
            LocalDate.of(1985, 5, 15),
            "456 Oak Ave",
            "CA",
            "Los Angeles",
            90210,
            "Aetna",
            "jane.smith@example.com"
        );

        // Act: Create the patient
        boolean result = patientDAO.createPatient(patient);

        // Assert: Verify creation was successful
        assertTrue(result, "Create should return true on success");

        // Verify the patient was persisted
        Patients retrieved = patientDAO.getPatientMRN(999998);
        assertNotNull(retrieved, "Patient should exist after creation");
        assertEquals("Jane", retrieved.getFname());
        assertEquals("Smith", retrieved.getLname());
        assertEquals(LocalDate.of(1985, 5, 15), retrieved.getDob());
        assertEquals("456 Oak Ave", retrieved.getAddress());
        assertEquals("CA", retrieved.getState());
        assertEquals("Los Angeles", retrieved.getCity());
        assertEquals(90210, retrieved.getZip());
        assertEquals("Aetna", retrieved.getInsurance());
        assertEquals("jane.smith@example.com", retrieved.getEmail());
    }

    @Test
    void testCreatePatient_DuplicateMRN() throws SQLException {
        // Arrange: Create and insert a patient
        Patients patient = new Patients(
            999997,
            "Bob",
            "Johnson",
            LocalDate.of(1975, 10, 20),
            "789 Pine St",
            "TX",
            "Houston",
            77001,
            "Cigna",
            "bob.johnson@example.com"
        );
        patientDAO.createPatient(patient);

        // Act & Assert: Attempt to create another patient with same MRN should throw exception
        Patients duplicatePatient = new Patients(
            999997,
            "Alice",
            "Brown",
            LocalDate.of(1980, 3, 10),
            "321 Elm St",
            "FL",
            "Miami",
            33101,
            "UnitedHealth",
            "alice.brown@example.com"
        );

        assertThrows(
            SQLException.class,
            () -> patientDAO.createPatient(duplicatePatient),
            "Create with duplicate MRN should throw SQLException"
        );
    }
}
