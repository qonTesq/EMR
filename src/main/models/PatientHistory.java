package main.models;

public class PatientHistory {
    private String id;
    private String patientID;
    private String procedureID;
    private String date;
    private double billing;
    private String doctor;

    public PatientHistory(String id, String patientID, String procedureID, String date, double billing, String doctor) {
        this.id = id;
        this.patientID = patientID;
        this.procedureID = procedureID;
        this.date = date;
        this.billing = billing;
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "PatientHistory ID: " + id + ", Patient ID: " + patientID + ", Procedure ID: " + procedureID +
                ", Date: " + date + ", Billing: $" + billing + ", Doctor: " + doctor;
    }
}
