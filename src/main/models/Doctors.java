package main.models;

/**
 * Represents a doctor in the Electronic Medical Records system.
 * This model class encapsulates information about medical doctors
 * who perform procedures on patients.
 *
 * Each doctor is uniquely identified by their ID.
 */
public class Doctors {

    /** Unique identifier for this doctor */
    private String id;

    /** Doctor's full name */
    private String name;

    public Doctors getId;

    /**
     * Constructs a new Doctors object with all required fields.
     *
     * @param id   Unique identifier for the doctor
     * @param name Doctor's full name
     */
    public Doctors(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Default constructor for creating an empty Doctors object.
     * Used by frameworks and for object initialization before setting fields.
     */
    public Doctors() {}

    /**
     * Gets the unique identifier for this doctor.
     *
     * @return the doctor ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this doctor.
     *
     * @param id the doctor ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the doctor's full name.
     *
     * @return the doctor's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the doctor's full name.
     *
     * @param name the doctor's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the doctor.
     * Includes the doctor ID and name in a readable format.
     *
     * @return formatted string containing doctor details
     */
    @Override
    public String toString() {
        return "Doctor ID: " + id + ", Name: " + name;
    }

    /**
     * Compares this doctor with another object for equality.
     * Two doctors are considered equal if they have the same ID.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctors)) return false;
        Doctors d = (Doctors) o;
        return id == d.id;
    }

    /**
     * Returns a hash code value for this doctor.
     * The hash code is based on the ID field.
     *
     * @return hash code value for this doctor
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
