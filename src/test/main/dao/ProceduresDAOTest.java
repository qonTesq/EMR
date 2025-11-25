package test.main.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import main.dao.DoctorDAO;
import main.dao.ProceduresDAO;
import main.models.Doctors;
import main.models.Procedures;
import main.util.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for ProceduresDAO update operations.
 * Tests the updateProcedure method to ensure it correctly updates procedure records in the database.
 */
public class ProceduresDAOTest {

    private Database db;
    private ProceduresDAO proceduresDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize test database connection
        db = new Database();
        proceduresDAO = new ProceduresDAO(db);

        // Create test doctors first (required for foreign key constraint)
        DoctorDAO doctorDAO = new DoctorDAO(db);
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
        // Clean up test data - remove test procedures if exist
        try (
            var stmt = db
                .getConnection()
                .prepareStatement(
                    "DELETE FROM procedures WHERE id = ? OR id = ? OR id = ?"
                )
        ) {
            stmt.setString(1, "PROC-TEST");
            stmt.setString(2, "PROC-TEST2");
            stmt.setString(3, "PROC-TEST3");
            stmt.executeUpdate();
        }
    }

    @Test
    void testUpdateProcedure_Success() throws SQLException {
        // Arrange: Create and insert a test procedure
        Procedures originalProcedure = new Procedures(
            "PROC-TEST",
            "Test Procedure",
            "A test procedure",
            30,
            "DR-001"
        );
        proceduresDAO.createProcedure(originalProcedure);

        // Act: Update procedure information
        Procedures updatedProcedure = new Procedures(
            "PROC-TEST",
            "Updated Procedure",
            "An updated test procedure",
            45,
            "DR-002"
        );
        boolean result = proceduresDAO.updateProcedure(updatedProcedure);

        // Assert: Verify update was successful
        assertTrue(result, "Update should return true on success");

        // Verify the changes were persisted
        Procedures retrieved = proceduresDAO.getProcedureID("PROC-TEST");
        assertNotNull(retrieved, "Procedure should exist after update");
        assertEquals("Updated Procedure", retrieved.getName());
        assertEquals("An updated test procedure", retrieved.getDescription());
        assertEquals(45, retrieved.getDuration());
        assertEquals("DR-002", retrieved.getDoctorId());
    }

    @Test
    void testUpdateProcedure_PartialUpdate() throws SQLException {
        // Arrange: Create and insert a test procedure
        Procedures originalProcedure = new Procedures(
            "PROC-TEST",
            "Test Procedure",
            "A test procedure",
            30,
            "DR-001"
        );
        proceduresDAO.createProcedure(originalProcedure);

        // Act: Update only some fields
        Procedures partialUpdate = new Procedures(
            "PROC-TEST",
            "Test Procedure",
            "A test procedure",
            30,
            "DR-001"
        );
        partialUpdate.setDuration(60); // Only change duration
        boolean result = proceduresDAO.updateProcedure(partialUpdate);

        // Assert: Verify update was successful and only specified field changed
        assertTrue(result, "Partial update should return true");
        Procedures retrieved = proceduresDAO.getProcedureID("PROC-TEST");
        assertEquals(60, retrieved.getDuration());
        assertEquals("Test Procedure", retrieved.getName()); // Unchanged
    }

    @Test
    void testUpdateProcedure_NonExistentProcedure() throws SQLException {
        // Arrange: Try to update a procedure that doesn't exist
        Procedures nonExistentProcedure = new Procedures(
            "PROC-TEST",
            "Non Existent",
            "Description",
            30,
            "DR-001"
        );

        // Act: Attempt update
        boolean result = proceduresDAO.updateProcedure(nonExistentProcedure);

        // Assert: Update should fail for non-existent procedure
        assertFalse(
            result,
            "Update should return false for non-existent procedure"
        );
    }

    @Test
    void testUpdateProcedure_InvalidData() throws SQLException {
        // Arrange: Create and insert a test procedure
        Procedures originalProcedure = new Procedures(
            "PROC-TEST",
            "Test Procedure",
            "A test procedure",
            30,
            "DR-001"
        );
        proceduresDAO.createProcedure(originalProcedure);

        // Act: Update with invalid data (null name)
        Procedures invalidProcedure = new Procedures(
            "PROC-TEST",
            null,
            "Description",
            30,
            "DR-001"
        );

        // Assert: Update with null data should return false (caught internally)
        boolean result = proceduresDAO.updateProcedure(invalidProcedure);
        assertFalse(result, "Update with null name should return false");
    }

    @Test
    void testCreateProcedure_Success() throws SQLException {
        // Arrange: Create a new procedure
        Procedures procedure = new Procedures(
            "PROC-TEST2",
            "New Procedure",
            "A new medical procedure",
            45,
            "DR-001"
        );

        // Act: Create the procedure
        boolean result = proceduresDAO.createProcedure(procedure);

        // Assert: Verify creation was successful
        assertTrue(result, "Create should return true on success");

        // Verify the procedure was persisted
        Procedures retrieved = proceduresDAO.getProcedureID("PROC-TEST2");
        assertNotNull(retrieved, "Procedure should exist after creation");
        assertEquals("PROC-TEST2", retrieved.getId());
        assertEquals("New Procedure", retrieved.getName());
        assertEquals("A new medical procedure", retrieved.getDescription());
        assertEquals(45, retrieved.getDuration());
        assertEquals("DR-001", retrieved.getDoctorId());
    }

    @Test
    void testCreateProcedure_DuplicateID() throws SQLException {
        // Arrange: Create and insert a procedure
        Procedures procedure = new Procedures(
            "PROC-TEST3",
            "Sample Procedure",
            "A sample procedure",
            60,
            "DR-001"
        );
        proceduresDAO.createProcedure(procedure);

        // Act & Assert: Attempt to create another procedure with same ID should throw exception
        Procedures duplicateProcedure = new Procedures(
            "PROC-TEST3",
            "Another Procedure",
            "Another description",
            30,
            "DR-002"
        );

        assertThrows(
            SQLException.class,
            () -> proceduresDAO.createProcedure(duplicateProcedure),
            "Create with duplicate ID should throw SQLException"
        );
    }
}
