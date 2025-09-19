package main;

import main.models.PatientHistory;
import main.models.Patients;
import main.models.Procedures;

public class App {
    public static void main(String[] args) {
        Patients patient1 = new Patients(12345, "John", "Doe", "1980/01/01", "123 Main St", "CA", "Los Angeles",
                "90001", "HealthPlus");
        System.out.println(patient1 + "\n");

        Procedures procedure1 = new Procedures("D0120", "Periodic oral evaluation");
        System.out.println(procedure1 + "\n");

        PatientHistory history1 = new PatientHistory("ph1", "12345", "D0120", "2023/10/01", 100, "Dr. Howard");
        System.out.println(history1);
    }
}