package main.models;

public class Procedures {

    private String id;

    private String name;

    private String description;

    private int duration;

    private String doctorId;

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

    public Procedures() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Procedures)) return false;
        Procedures p = (Procedures) o;
        return id != null && id.equals(p.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
