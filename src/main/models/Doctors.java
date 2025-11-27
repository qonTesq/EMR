package main.models;

public class Doctors {

    private String id;

    private String name;

    public Doctors getId;

    public Doctors(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Doctors() {}

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

    @Override
    public String toString() {
        return "Doctor ID: " + id + ", Name: " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctors)) return false;
        Doctors d = (Doctors) o;
        return id == d.id;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
