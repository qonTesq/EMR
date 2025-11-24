package main.models;

/**
 * Represents a medical procedure in the Electronic Medical Records system.
 * This model class encapsulates information about available medical procedures
 * that can be performed on patients.
 *
 * Each procedure is uniquely identified by its ID and contains detailed
 * information including description, duration, and the performing doctor.
 */
public class Procedures {

    /** Unique identifier for this medical procedure */
    private String id;

    /** Descriptive name of the medical procedure */
    private String name;

    /** Detailed description of the procedure */
    private String description;

    /** Duration of the procedure in minutes */
    private int duration;

    /** ID of the doctor who performs this procedure */
    private String doctorId;

    /**
     * Constructs a new Procedures object with all required fields.
     *
     * @param id          Unique identifier for the procedure
     * @param name        Descriptive name of the procedure
     * @param description Detailed description of the procedure
     * @param duration    Duration in minutes
     * @param doctorId    ID of the performing doctor
     */
    public Procedures(
        String id,
        String name,
        String description,
        int duration,
        String doctorId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.doctorId = doctorId;
    }

    /**
     * Default constructor for creating an empty Procedures object.
     * Used by frameworks and for object initialization before setting fields.
     */
    public Procedures() {}

    /**
     * Gets the unique identifier for this procedure.
     *
     * @return the procedure ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this procedure.
     *
     * @param id the procedure ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the descriptive name of this procedure.
     *
     * @return the procedure name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the descriptive name of this procedure.
     *
     * @param name the procedure name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the detailed description of this procedure.
     *
     * @return the procedure description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the detailed description of this procedure.
     *
     * @param description the procedure description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the duration of this procedure in minutes.
     *
     * @return the procedure duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of this procedure in minutes.
     *
     * @param duration the procedure duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the ID of the doctor who performs this procedure.
     *
     * @return the doctor ID
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the ID of the doctor who performs this procedure.
     *
     * @param doctorId the doctor ID to set
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Returns a string representation of the procedure.
     * Includes the procedure ID, name, description, duration, and doctor ID in a
     * readable format.
     *
     * @return formatted string containing procedure details
     */
    @Override
    public String toString() {
        return (
            "Procedure ID: " +
            id +
            ", Name: " +
            name +
            ", Description: " +
            description +
            ", Duration: " +
            duration +
            " minutes, Doctor ID: " +
            doctorId
        );
    }

    /**
     * Compares this procedure with another object for equality.
     * Two procedures are considered equal if they have the same ID.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Procedures)) return false;
        Procedures p = (Procedures) o;
        return id != null && id.equals(p.id);
    }

    /**
     * Returns a hash code value for this procedure.
     * The hash code is based on the ID field.
     *
     * @return hash code value for this procedure
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
