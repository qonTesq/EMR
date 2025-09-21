package main.models;

import java.time.LocalDate;

/**
 * Represents a patient history record in the Electronic Medical Records system.
 * This model class encapsulates information about medical procedures performed
 * on patients, including billing information and the attending doctor.
 *
 * Each patient history record is uniquely identified by its ID and links a
 * patient to a specific medical procedure with associated details.
 */
public class PatientHistory {

    /** Unique identifier for this patient history record */
    private String id;

    /**
     * ID of the patient this history record belongs to (references Patients.mrn)
     */
    private int patientID;

    /** ID of the procedure performed (references Procedures.id) */
    private String procedureID;

    /** Date when the procedure was performed */
    private LocalDate date;

    /** Billing amount for the procedure in dollars */
    private double billing;

    /** Name of the doctor who performed the procedure */
    private String doctor;

    /**
     * Constructs a new PatientHistory object with all required fields.
     *
     * @param id          Unique identifier for the history record
     * @param patientID   ID of the patient (must match an existing patient MRN)
     * @param procedureID ID of the procedure performed
     * @param date        Date when the procedure was performed
     * @param billing     Billing amount for the procedure
     * @param doctor      Name of the attending doctor
     */
    public PatientHistory(String id, int patientID, String procedureID, LocalDate date, double billing, String doctor) {
        this.id = id;
        this.patientID = patientID;
        this.procedureID = procedureID;
        this.date = date;
        this.billing = billing;
        this.doctor = doctor;
    }

    /**
     * Default constructor for creating an empty PatientHistory object.
     * Used by frameworks and for object initialization before setting fields.
     */
    public PatientHistory() {
    }

    /**
     * Gets the unique identifier for this patient history record.
     *
     * @return the history record ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this patient history record.
     *
     * @param id the history record ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the patient ID this history record belongs to.
     *
     * @return the patient ID
     */
    public int getPatientID() {
        return patientID;
    }

    /**
     * Sets the patient ID this history record belongs to.
     *
     * @param patientID the patient ID to set
     */
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    /**
     * Gets the procedure ID for the medical procedure performed.
     *
     * @return the procedure ID
     */
    public String getProcedureID() {
        return procedureID;
    }

    /**
     * Sets the procedure ID for the medical procedure performed.
     *
     * @param procedureID the procedure ID to set
     */
    public void setProcedureID(String procedureID) {
        this.procedureID = procedureID;
    }

    /**
     * Gets the date when the procedure was performed.
     *
     * @return the procedure date as LocalDate
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date when the procedure was performed.
     *
     * @param date the procedure date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the billing amount for the procedure.
     *
     * @return the billing amount in dollars
     */
    public double getBilling() {
        return billing;
    }

    /**
     * Sets the billing amount for the procedure.
     *
     * @param billing the billing amount to set
     */
    public void setBilling(double billing) {
        this.billing = billing;
    }

    /**
     * Gets the name of the doctor who performed the procedure.
     *
     * @return the doctor's name
     */
    public String getDoctor() {
        return doctor;
    }

    /**
     * Sets the name of the doctor who performed the procedure.
     *
     * @param doctor the doctor's name to set
     */
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    /**
     * Returns a string representation of the patient history record.
     * Includes all relevant information in a readable format.
     *
     * @return formatted string containing patient history details
     */
    @Override
    public String toString() {
        return "PatientHistory ID: " + id + ", Patient ID: " + patientID + ", Procedure ID: " + procedureID +
                ", Date: " + date + ", Billing: $" + billing + ", Doctor: " + doctor;
    }

    /**
     * Compares this patient history record with another object for equality.
     * Two patient history records are considered equal if they have the same ID.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PatientHistory))
            return false;
        PatientHistory ph = (PatientHistory) o;
        return id != null && id.equals(ph.id);
    }

    /**
     * Returns a hash code value for this patient history record.
     * The hash code is based on the ID field.
     *
     * @return hash code value for this patient history record
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
