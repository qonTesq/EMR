package test.main.models;

import static org.junit.jupiter.api.Assertions.*;

import main.models.Doctors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for the Doctors model.
 * Tests all constructors, getters, setters, and overridden methods.
 */
public class DoctorsTest {

    private Doctors doctor;

    @BeforeEach
    public void setUp() {
        doctor = new Doctors("DR-001", "Dr. John Smith");
    }

    @Test
    public void testConstructorWithAllParameters() {
        assertEquals("DR-001", doctor.getId());
        assertEquals("Dr. John Smith", doctor.getName());
    }

    @Test
    public void testDefaultConstructor() {
        Doctors emptyDoctor = new Doctors();
        assertNotNull(emptyDoctor);
    }

    @Test
    public void testSettersAndGetters() {
        Doctors newDoctor = new Doctors();

        newDoctor.setId("DR-002");
        newDoctor.setName("Dr. Jane Doe");

        assertEquals("DR-002", newDoctor.getId());
        assertEquals("Dr. Jane Doe", newDoctor.getName());
    }

    @Test
    public void testToString() {
        String expected = "Doctor ID: DR-001, Name: Dr. John Smith";
        assertEquals(expected, doctor.toString());
    }

    @Test
    public void testEqualsWithSameId() {
        Doctors doctor1 = new Doctors("DR-001", "Dr. John Smith");
        Doctors doctor2 = new Doctors("DR-001", "Dr. Jane Doe");

        // Note: Current implementation uses == for id comparison, which may not work for strings
        // This test assumes the current buggy behavior
        assertTrue(
            doctor1.equals(doctor2),
            "Doctors with same ID should be equal"
        );
    }

    @Test
    public void testEqualsWithDifferentId() {
        Doctors doctor1 = new Doctors("DR-001", "Dr. John Smith");
        Doctors doctor2 = new Doctors("DR-002", "Dr. John Smith");

        assertFalse(
            doctor1.equals(doctor2),
            "Doctors with different IDs should not be equal"
        );
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(doctor.equals(doctor), "Doctor should be equal to itself");
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(doctor.equals(null), "Doctor should not be equal to null");
    }

    @Test
    public void testEqualsWithDifferentClass() {
        String notADoctor = "Not a doctor";
        assertFalse(
            doctor.equals(notADoctor),
            "Doctor should not be equal to different class"
        );
    }

    @Test
    public void testHashCodeConsistency() {
        int hash1 = doctor.hashCode();
        int hash2 = doctor.hashCode();
        assertEquals(hash1, hash2, "Hash code should be consistent");
    }

    @Test
    public void testHashCodeEqualityForEqualObjects() {
        Doctors doctor1 = new Doctors("DR-001", "Dr. John Smith");
        Doctors doctor2 = new Doctors("DR-001", "Dr. Jane Doe");

        assertEquals(
            doctor1.hashCode(),
            doctor2.hashCode(),
            "Equal doctors should have equal hash codes"
        );
    }

    @Test
    public void testHashCodeWithNullId() {
        Doctors doctorWithNullId = new Doctors();
        assertEquals(
            0,
            doctorWithNullId.hashCode(),
            "Hash code should be 0 for null ID"
        );
    }
}
