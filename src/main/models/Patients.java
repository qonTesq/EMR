package main.models;

import java.time.LocalDate;

public class Patients {

    private int mrn;

    private String fname;

    private String lname;

    private LocalDate dob;

    private String address;

    private String state;

    private String city;

    private int zip;

    private String insurance;

    private String email;

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

    public Patients() {}

    public int getMrn() {
        return mrn;
    }

    public void setMrn(int mrn) {
        this.mrn = mrn;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patients)) return false;
        Patients p = (Patients) o;
        return mrn == p.mrn;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(mrn);
    }
}
