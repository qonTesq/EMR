package main.models;

public class Procedures {
    private String id;
    private String name;

    public Procedures(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Procedure ID: " + id + ", Name: " + name;
    }
}
