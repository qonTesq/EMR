package test.main.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import main.dao.DoctorDAO;
import main.models.Doctors;
import main.util.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for DoctorDAO CRUD operations.
 * Tests the create, read, and update methods to ensure they correctly manage doctor records in the database.
 */
public class DoctorDAOTest {

    private Database db;
    private DoctorDAO doctorDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize test database connection
        db = new Database();
        doctorDAO = new DoctorDAO(db);

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
        // Clean up test data - remove test doctors if exist
        try (
            var stmt = db
                .getConnection()
                .prepareStatement(
                    "DELETE FROM doctors WHERE id = ? OR id = ? OR id = ?"
                )
        ) {
            stmt.setString(1, "DR-TEST");
            stmt.setString(2, "DR-TEST2");
            stmt.setString(3, "DR-TEST3");
            stmt.executeUpdate();
        }
    }

    @Test
    void testUpdateDoctor_Success() throws SQLException {
        // Arrange: Create and insert a test doctor
        Doctors originalDoctor = new Doctors("DR-TEST", "Dr. John Smith");
        doctorDAO.createDoctor(originalDoctor);

        // Act: Update doctor name
        Doctors updatedDoctor = new Doctors("DR-TEST", "Dr. Jane Doe");
        boolean result = doctorDAO.updateDoctor(updatedDoctor);

        // Assert: Verify update was successful
        assertTrue(result, "Update should return true on success");

        // Verify the changes were persisted
        Doctors retrieved = doctorDAO.getDoctorID("DR-TEST");
        assertNotNull(retrieved, "Doctor should exist after update");
        assertEquals("Dr. Jane Doe", retrieved.getName());
    }

    @Test
    void testUpdateDoctor_NonExistentDoctor() throws SQLException {
        // Arrange: Try to update a doctor that doesn't exist
        Doctors nonExistentDoctor = new Doctors("DR-TEST", "Dr. Non Existent");

        // Act: Attempt update
        boolean result = doctorDAO.updateDoctor(nonExistentDoctor);

        // Assert: Update should fail for non-existent doctor
        assertFalse(
            result,
            "Update should return false for non-existent doctor"
        );
    }

    @Test
    void testUpdateDoctor_InvalidId() throws SQLException {
        // Arrange: Try to update with invalid ID format
        Doctors invalidDoctor = new Doctors("INVALID", "Dr. Test");

        // Act: Attempt update
        boolean result = doctorDAO.updateDoctor(invalidDoctor);

        // Assert: Update should fail for invalid ID
        assertFalse(result, "Update should return false for invalid ID");
    }

    @Test
    void testUpdateDoctor_EmptyName() throws SQLException {
        // Arrange: Create and insert a test doctor
        Doctors originalDoctor = new Doctors("DR-TEST", "Dr. John Smith");
        doctorDAO.createDoctor(originalDoctor);

        // Act: Update with empty name
        Doctors updatedDoctor = new Doctors("DR-TEST", "");
        boolean result = doctorDAO.updateDoctor(updatedDoctor);

        // Assert: Update should succeed (empty name is allowed in this implementation)
        assertTrue(result, "Update with empty name should succeed");
        Doctors retrieved = doctorDAO.getDoctorID("DR-TEST");
        assertEquals("", retrieved.getName());
    }

    @Test
    void testCreateDoctor_Success() throws SQLException {
        // Arrange: Create a new doctor
        Doctors doctor = new Doctors("DR-TEST2", "Dr. Jane Doe");

        // Act: Create the doctor
        boolean result = doctorDAO.createDoctor(doctor);

        // Assert: Verify creation was successful
        assertTrue(result, "Create should return true on success");

        // Verify the doctor was persisted
        Doctors retrieved = doctorDAO.getDoctorID("DR-TEST2");
        assertNotNull(retrieved, "Doctor should exist after creation");
        assertEquals("DR-TEST2", retrieved.getId());
        assertEquals("Dr. Jane Doe", retrieved.getName());
    }

    @Test
    void testCreateDoctor_DuplicateID() throws SQLException {
        // Arrange: Create and insert a doctor
        Doctors doctor = new Doctors("DR-TEST3", "Dr. Bob Wilson");
        doctorDAO.createDoctor(doctor);

        // Act & Assert: Attempt to create another doctor with same ID should throw exception
        Doctors duplicateDoctor = new Doctors("DR-TEST3", "Dr. Alice Brown");

        assertThrows(
            SQLException.class,
            () -> doctorDAO.createDoctor(duplicateDoctor),
            "Create with duplicate ID should throw SQLException"
        );
    }
}
