package test.main.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import main.models.Patients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for the Patients model.
 * Tests all constructors, getters, setters, and overridden methods.
 */
public class PatientsTest {

    private Patients patient;
    private LocalDate testDob;

    @BeforeEach
    public void setUp() {
        testDob = LocalDate.of(1990, 5, 15);
        patient = new Patients(
            12345,
            "John",
            "Doe",
            testDob,
            "123 Main St",
            "California",
            "Los Angeles",
            90001,
            "Blue Cross",
            "john.doe@email.com"
        );
    }

    @Test
    public void testConstructorWithAllParameters() {
        assertEquals(12345, patient.getMrn());
        assertEquals("John", patient.getFname());
        assertEquals("Doe", patient.getLname());
        assertEquals(testDob, patient.getDob());
        assertEquals("123 Main St", patient.getAddress());
        assertEquals("California", patient.getState());
        assertEquals("Los Angeles", patient.getCity());
        assertEquals(90001, patient.getZip());
        assertEquals("Blue Cross", patient.getInsurance());
        assertEquals("john.doe@email.com", patient.getEmail());
    }

    @Test
    public void testDefaultConstructor() {
        Patients emptyPatient = new Patients();
        assertNotNull(emptyPatient);
    }

    @Test
    public void testSettersAndGetters() {
        Patients newPatient = new Patients();

        newPatient.setMrn(54321);
        newPatient.setFname("Jane");
        newPatient.setLname("Smith");
        LocalDate newDob = LocalDate.of(1985, 10, 20);
        newPatient.setDob(newDob);
        newPatient.setAddress("456 Oak Ave");
        newPatient.setState("New York");
        newPatient.setCity("New York City");
        newPatient.setZip(10001);
        newPatient.setInsurance("Aetna");
        newPatient.setEmail("jane.smith@email.com");

        assertEquals(54321, newPatient.getMrn());
        assertEquals("Jane", newPatient.getFname());
        assertEquals("Smith", newPatient.getLname());
        assertEquals(newDob, newPatient.getDob());
        assertEquals("456 Oak Ave", newPatient.getAddress());
        assertEquals("New York", newPatient.getState());
        assertEquals("New York City", newPatient.getCity());
        assertEquals(10001, newPatient.getZip());
        assertEquals("Aetna", newPatient.getInsurance());
        assertEquals("jane.smith@email.com", newPatient.getEmail());
    }

    @Test
    public void testToString() {
        String expected =
            "Patient MRN: 12345, Name: John Doe, DOB: 1990-05-15, " +
            "Address: 123 Main St, City: Los Angeles, State: California, " +
            "Zip: 90001, Insurance: Blue Cross, Email: john.doe@email.com";
        assertEquals(expected, patient.toString());
    }

    @Test
    public void testEqualsWithSameMrn() {
        Patients patient1 = new Patients(
            12345,
            "John",
            "Doe",
            testDob,
            "123 Main St",
            "California",
            "Los Angeles",
            90001,
            "Blue Cross",
            "john.doe@email.com"
        );
        Patients patient2 = new Patients(
            12345,
            "Jane",
            "Smith",
            LocalDate.of(1985, 1, 1),
            "456 Oak Ave",
            "New York",
            "New York City",
            10001,
            "Aetna",
            "jane@email.com"
        );

        assertTrue(
            patient1.equals(patient2),
            "Patients with same MRN should be equal"
        );
    }

    @Test
    public void testEqualsWithDifferentMrn() {
        Patients patient1 = new Patients(
            12345,
            "John",
            "Doe",
            testDob,
            "123 Main St",
            "California",
            "Los Angeles",
            90001,
            "Blue Cross",
            "john.doe@email.com"
        );
        Patients patient2 = new Patients(
            54321,
            "John",
            "Doe",
            testDob,
            "123 Main St",
            "California",
            "Los Angeles",
            90001,
            "Blue Cross",
            "john.doe@email.com"
        );

        assertFalse(
            patient1.equals(patient2),
            "Patients with different MRN should not be equal"
        );
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(
            patient.equals(patient),
            "Patient should be equal to itself"
        );
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(
            patient.equals(null),
            "Patient should not be equal to null"
        );
    }

    @Test
    public void testEqualsWithDifferentClass() {
        String notAPatient = "Not a patient";
        assertFalse(
            patient.equals(notAPatient),
            "Patient should not be equal to different class"
        );
    }

    @Test
    public void testHashCodeConsistency() {
        int hash1 = patient.hashCode();
        int hash2 = patient.hashCode();
        assertEquals(hash1, hash2, "Hash code should be consistent");
    }

    @Test
    public void testHashCodeEqualityForEqualObjects() {
        Patients patient1 = new Patients(
            12345,
            "John",
            "Doe",
            testDob,
            "123 Main St",
            "California",
            "Los Angeles",
            90001,
            "Blue Cross",
            "john.doe@email.com"
        );
        Patients patient2 = new Patients(
            12345,
            "Jane",
            "Smith",
            LocalDate.of(1985, 1, 1),
            "456 Oak Ave",
            "New York",
            "New York City",
            10001,
            "Aetna",
            "jane@email.com"
        );

        assertEquals(
            patient1.hashCode(),
            patient2.hashCode(),
            "Equal patients should have equal hash codes"
        );
    }
}
