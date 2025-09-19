package main.models;

public class Patients {
    private int mrn;
    private String fname;
    private String lname;
    private String dob;
    private String address;
    private String state;
    private String city;
    private String zip;
    private String insurance;

    public Patients(int mrn, String fname, String lname, String dob, String address, String state, String city,
            String zip, String insurance) {
        this.mrn = mrn;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.address = address;
        this.state = state;
        this.city = city;
        this.zip = zip;
        this.insurance = insurance;
    }

    @Override
    public String toString() {
        return "Patient MRN: " + mrn + ", Name: " + fname + " " + lname + ", DOB: " + dob + ", Address: " + address
                + ", City: " + city + ", State: " + state + ", Zip: " + zip + ", Insurance: " + insurance;
    }
}
