package main.models;

import java.time.LocalDate;

public class PatientHistory {

    private String id;

    private int patientId;

    private String procedureId;

    private LocalDate date;

    private double billing;

    private String doctorId;

    public PatientHistory(
        String id,
        int patientId,
        String procedureId,
        LocalDate date,
        double billing,
        String doctorId
    ) {
        this.id = id;
        this.patientId = patientId;
        this.procedureId = procedureId;
        this.date = date;
        this.billing = billing;
        this.doctorId = doctorId;
    }

    public PatientHistory() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getBilling() {
        return billing;
    }

    public void setBilling(double billing) {
        this.billing = billing;
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
            "PatientHistory ID: " +
            id +
            ", Patient ID: " +
            patientId +
            ", Procedure ID: " +
            procedureId +
            ", Date: " +
            date +
            ", Billing: $" +
            billing +
            ", Doctor ID: " +
            doctorId
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientHistory)) return false;
        PatientHistory ph = (PatientHistory) o;
        return id != null && id.equals(ph.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
