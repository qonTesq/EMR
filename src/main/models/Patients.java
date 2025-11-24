package main.models;

import java.time.LocalDate;

/**
 * Represents a patient in the Electronic Medical Records system.
 * This model class encapsulates all patient information including personal
 * details, contact information, and insurance data.
 *
 * Each patient is uniquely identified by their Medical Record Number (MRN).
 */
public class Patients {

    /** Medical Record Number - unique identifier for each patient */
    private int mrn;

    /** Patient's first name */
    private String fname;

    /** Patient's last name */
    private String lname;

    /** Patient's date of birth */
    private LocalDate dob;

    /** Patient's street address */
    private String address;

    /** Patient's state of residence */
    private String state;

    /** Patient's city of residence */
    private String city;

    /** Patient's zip code (postal code) */
    private int zip;

    /** Patient's insurance provider information */
    private String insurance;

    /** Patient's email address */
    private String email;

    /**
     * Constructs a new Patients object with all required fields.
     *
     * @param mrn       Medical Record Number (unique identifier)
     * @param fname     Patient's first name
     * @param lname     Patient's last name
     * @param dob       Patient's date of birth
     * @param address   Patient's street address
     * @param state     Patient's state
     * @param city      Patient's city
     * @param zip       Patient's zip code
     * @param insurance Patient's insurance information
     * @param email     Patient's email address
     */
    public Patients(
        int mrn,
        String fname,
        String lname,
        LocalDate dob,
        String address,
        String state,
        String city,
        int zip,
        String insurance,
        String email
    ) {
        this.mrn = mrn;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.address = address;
        this.state = state;
        this.city = city;
        this.zip = zip;
        this.insurance = insurance;
        this.email = email;
    }

    /**
     * Default constructor for creating an empty Patients object.
     * Used by frameworks and for object initialization before setting fields.
     */
    public Patients() {}

    /**
     * Gets the Medical Record Number.
     *
     * @return the patient's MRN
     */
    public int getMrn() {
        return mrn;
    }

    /**
     * Sets the Medical Record Number.
     *
     * @param mrn the MRN to set
     */
    public void setMrn(int mrn) {
        this.mrn = mrn;
    }

    /**
     * Gets the patient's first name.
     *
     * @return the first name
     */
    public String getFname() {
        return fname;
    }

    /**
     * Sets the patient's first name.
     *
     * @param fname the first name to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * Gets the patient's last name.
     *
     * @return the last name
     */
    public String getLname() {
        return lname;
    }

    /**
     * Sets the patient's last name.
     *
     * @param lname the last name to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * Gets the patient's date of birth.
     *
     * @return the date of birth as LocalDate
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Sets the patient's date of birth.
     *
     * @param dob the date of birth to set
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    /**
     * Gets the patient's street address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the patient's street address.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the patient's state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the patient's state.
     *
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the patient's city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the patient's city.
     *
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the patient's zip code.
     *
     * @return the zip code
     */
    public int getZip() {
        return zip;
    }

    /**
     * Sets the patient's zip code.
     *
     * @param zip the zip code to set
     */
    public void setZip(int zip) {
        this.zip = zip;
    }

    /**
     * Gets the patient's insurance information.
     *
     * @return the insurance information
     */
    public String getInsurance() {
        return insurance;
    }

    /**
     * Sets the patient's insurance information.
     *
     * @param insurance the insurance information to set
     */
    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    /**
     * Gets the patient's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the patient's email address.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a string representation of the patient.
     * Includes all patient information in a readable format.
     *
     * @return formatted string containing patient details
     */
    @Override
    public String toString() {
        return (
            "Patient MRN: " +
            mrn +
            ", Name: " +
            fname +
            " " +
            lname +
            ", DOB: " +
            dob +
            ", Address: " +
            address +
            ", City: " +
            city +
            ", State: " +
            state +
            ", Zip: " +
            zip +
            ", Insurance: " +
            insurance +
            ", Email: " +
            email
        );
    }

    /**
     * Compares this patient with another object for equality.
     * Two patients are considered equal if they have the same MRN.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patients)) return false;
        Patients p = (Patients) o;
        return mrn == p.mrn;
    }

    /**
     * Returns a hash code value for this patient.
     * The hash code is based on the MRN field.
     *
     * @return hash code value for this patient
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(mrn);
    }
}
