package main;

import main.cli.MainCLI;
import main.util.Database;

public class App {

    public static void main(String[] args) {
        Database db = null;

        try {
            db = new Database();

            new MainCLI(db).start();
        } catch (RuntimeException e) {
            System.err.println("Fatal error: " + e.getMessage());
            System.exit(1);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
