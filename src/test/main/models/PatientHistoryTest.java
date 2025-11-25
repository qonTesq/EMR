package test.main.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import main.models.PatientHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for the PatientHistory model.
 * Tests all constructors, getters, setters, and overridden methods.
 */
public class PatientHistoryTest {

    private PatientHistory history;
    private LocalDate testDate;

    @BeforeEach
    public void setUp() {
        testDate = LocalDate.of(2023, 5, 15);
        history = new PatientHistory(
            "HIST-001",
            12345,
            "PROC-001",
            testDate,
            1500.00,
            "DR-001"
        );
    }

    @Test
    public void testConstructorWithAllParameters() {
        assertEquals("HIST-001", history.getId());
        assertEquals(12345, history.getPatientId());
        assertEquals("PROC-001", history.getProcedureId());
        assertEquals(testDate, history.getDate());
        assertEquals(1500.00, history.getBilling());
        assertEquals("DR-001", history.getDoctorId());
    }

    @Test
    public void testDefaultConstructor() {
        PatientHistory emptyHistory = new PatientHistory();
        assertNotNull(emptyHistory);
    }

    @Test
    public void testSettersAndGetters() {
        PatientHistory newHistory = new PatientHistory();

        newHistory.setId("HIST-002");
        newHistory.setPatientId(54321);
        newHistory.setProcedureId("PROC-002");
        LocalDate newDate = LocalDate.of(2023, 6, 20);
        newHistory.setDate(newDate);
        newHistory.setBilling(2000.00);
        newHistory.setDoctorId("DR-002");

        assertEquals("HIST-002", newHistory.getId());
        assertEquals(54321, newHistory.getPatientId());
        assertEquals("PROC-002", newHistory.getProcedureId());
        assertEquals(newDate, newHistory.getDate());
        assertEquals(2000.00, newHistory.getBilling());
        assertEquals("DR-002", newHistory.getDoctorId());
    }

    @Test
    public void testToString() {
        String expected =
            "PatientHistory ID: HIST-001, Patient ID: 12345, Procedure ID: PROC-001, " +
            "Date: 2023-05-15, Billing: $1500.0, Doctor ID: DR-001";
        assertEquals(expected, history.toString());
    }

    @Test
    public void testEqualsWithSameId() {
        PatientHistory history1 = new PatientHistory(
            "HIST-001",
            12345,
            "PROC-001",
            testDate,
            1500.00,
            "DR-001"
        );
        PatientHistory history2 = new PatientHistory(
            "HIST-001",
            54321,
            "PROC-002",
            LocalDate.of(2023, 6, 20),
            2000.00,
            "DR-002"
        );

        assertTrue(history1.equals(history2), "PatientHistory with same ID should be equal");
    }

    @Test
    public void testEqualsWithDifferentId() {
        PatientHistory history1 = new PatientHistory(
            "HIST-001",
            12345,
            "PROC-001",
            testDate,
            1500.00,
            "DR-001"
        );
        PatientHistory history2 = new PatientHistory(
            "HIST-002",
            12345,
            "PROC-001",
            testDate,
            1500.00,
            "DR-001"
        );

        assertFalse(history1.equals(history2), "PatientHistory with different IDs should not be equal");
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(history.equals(history), "PatientHistory should be equal to itself");
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(history.equals(null), "PatientHistory should not be equal to null");
    }

    @Test
    public void testEqualsWithDifferentClass() {
        String notAHistory = "Not a patient history";
        assertFalse(history.equals(notAHistory), "PatientHistory should not be equal to different class");
    }

    @Test
    public void testEqualsWithNullId() {
        PatientHistory historyWithNullId = new PatientHistory();
        PatientHistory history2 = new PatientHistory();
        assertFalse(historyWithNullId.equals(history2), "PatientHistory with null ID should not be equal");
    }

    @Test
    public void testHashCodeConsistency() {
        int hash1 = history.hashCode();
        int hash2 = history.hashCode();
        assertEquals(hash1, hash2, "Hash code should be consistent");
    }

    @Test
    public void testHashCodeEqualityForEqualObjects() {
        PatientHistory history1 = new PatientHistory(
            "HIST-001",
            12345,
            "PROC-001",
            testDate,
            1500.00,
            "DR-001"
        );
        PatientHistory history2 = new PatientHistory(
            "HIST-001",
            54321,
            "PROC-002",
            LocalDate.of(2023, 6, 20),
            2000.00,
            "DR-002"
        );

        assertEquals(history1.hashCode(), history2.hashCode(), "Equal PatientHistory should have equal hash codes");
    }

    @Test
    public void testHashCodeWithNullId() {
        PatientHistory historyWithNullId = new PatientHistory();
        assertEquals(0, historyWithNullId.hashCode(), "Hash code should be 0 for null ID");
    }
}
