package test.main.models;

import static org.junit.jupiter.api.Assertions.*;

import main.models.Procedures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for the Procedures model.
 * Tests all constructors, getters, setters, and overridden methods.
 */
public class ProceduresTest {

    private Procedures procedure;

    @BeforeEach
    public void setUp() {
        procedure = new Procedures(
            "PROC-001",
            "Test Procedure",
            "A test procedure",
            30,
            "DR-001"
        );
    }

    @Test
    public void testConstructorWithAllParameters() {
        assertEquals("PROC-001", procedure.getId());
        assertEquals("Test Procedure", procedure.getName());
        assertEquals("A test procedure", procedure.getDescription());
        assertEquals(30, procedure.getDuration());
        assertEquals("DR-001", procedure.getDoctorId());
    }

    @Test
    public void testDefaultConstructor() {
        Procedures emptyProcedure = new Procedures();
        assertNotNull(emptyProcedure);
    }

    @Test
    public void testSettersAndGetters() {
        Procedures newProcedure = new Procedures();

        newProcedure.setId("PROC-002");
        newProcedure.setName("New Procedure");
        newProcedure.setDescription("A new procedure description");
        newProcedure.setDuration(45);
        newProcedure.setDoctorId("DR-002");

        assertEquals("PROC-002", newProcedure.getId());
        assertEquals("New Procedure", newProcedure.getName());
        assertEquals(
            "A new procedure description",
            newProcedure.getDescription()
        );
        assertEquals(45, newProcedure.getDuration());
        assertEquals("DR-002", newProcedure.getDoctorId());
    }

    @Test
    public void testToString() {
        String expected =
            "Procedure ID: PROC-001, Name: Test Procedure, Description: A test procedure, Duration: 30 minutes, Doctor ID: DR-001";
        assertEquals(expected, procedure.toString());
    }

    @Test
    public void testEqualsWithSameId() {
        Procedures procedure1 = new Procedures(
            "PROC-001",
            "Test Procedure",
            "Description",
            30,
            "DR-001"
        );
        Procedures procedure2 = new Procedures(
            "PROC-001",
            "Different Name",
            "Different desc",
            60,
            "DR-002"
        );

        assertTrue(
            procedure1.equals(procedure2),
            "Procedures with same ID should be equal"
        );
    }

    @Test
    public void testEqualsWithDifferentId() {
        Procedures procedure1 = new Procedures(
            "PROC-001",
            "Test Procedure",
            "Description",
            30,
            "DR-001"
        );
        Procedures procedure2 = new Procedures(
            "PROC-002",
            "Test Procedure",
            "Description",
            30,
            "DR-001"
        );

        assertFalse(
            procedure1.equals(procedure2),
            "Procedures with different IDs should not be equal"
        );
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(
            procedure.equals(procedure),
            "Procedure should be equal to itself"
        );
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(
            procedure.equals(null),
            "Procedure should not be equal to null"
        );
    }

    @Test
    public void testEqualsWithDifferentClass() {
        String notAProcedure = "Not a procedure";
        assertFalse(
            procedure.equals(notAProcedure),
            "Procedure should not be equal to different class"
        );
    }

    @Test
    public void testEqualsWithNullId() {
        Procedures procedureWithNullId = new Procedures();
        Procedures procedure2 = new Procedures();
        assertFalse(
            procedureWithNullId.equals(procedure2),
            "Procedures with null ID should not be equal"
        );
    }

    @Test
    public void testHashCodeConsistency() {
        int hash1 = procedure.hashCode();
        int hash2 = procedure.hashCode();
        assertEquals(hash1, hash2, "Hash code should be consistent");
    }

    @Test
    public void testHashCodeEqualityForEqualObjects() {
        Procedures procedure1 = new Procedures(
            "PROC-001",
            "Test Procedure",
            "Description",
            30,
            "DR-001"
        );
        Procedures procedure2 = new Procedures(
            "PROC-001",
            "Different Name",
            "Different desc",
            60,
            "DR-002"
        );

        assertEquals(
            procedure1.hashCode(),
            procedure2.hashCode(),
            "Equal procedures should have equal hash codes"
        );
    }

    @Test
    public void testHashCodeWithNullId() {
        Procedures procedureWithNullId = new Procedures();
        assertEquals(
            0,
            procedureWithNullId.hashCode(),
            "Hash code should be 0 for null ID"
        );
    }
}
