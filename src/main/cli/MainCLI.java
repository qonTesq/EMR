package main.cli;

import main.util.Database;

public class MainCLI extends CLI {

    private static final String WELCOME_MESSAGE =
        "Welcome to EMR Management System\n";

    private static final String GOODBYE_MESSAGE =
        "Thank you for using EMR Management System. Goodbye\n";

    private final Database db;

    public MainCLI(Database db) {
        super();
        this.db = db;
    }

    @Override
    public void start() {
        System.out.println(WELCOME_MESSAGE);

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");

            System.out.println();

            switch (choice) {
                case 1:
                    new PatientsCLI(db).start();
                    System.out.println();
                    break;
                case 2:
                    new PatientHistoryCLI(db).start();
                    System.out.println();
                    break;
                case 3:
                    new ProceduresCLI(db).start();
                    System.out.println();
                    break;
                case 4:
                    new DoctorsCLI(db).start();
                    System.out.println();
                    break;
                case 5:
                    running = false;
                    System.out.println(GOODBYE_MESSAGE);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println();
            }
        }
    }

    private void showMainMenu() {
        System.out.println("EMR Management System");
        System.out.println();
        System.out.println("1. Patients");
        System.out.println("2. Patient History");
        System.out.println("3. Procedures");
        System.out.println("4. Doctors");
        System.out.println("5. Exit");
    }
}
