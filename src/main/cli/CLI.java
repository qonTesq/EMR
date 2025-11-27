package main.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public abstract class CLI {

    protected final Scanner scanner;

    protected static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected CLI() {
        this.scanner = new Scanner(System.in);
    }

    protected int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid input. Please enter a valid number."
                );
            }
        }
    }

    protected double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid input. Please enter a valid number."
                );
            }
        }
    }

    protected String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    protected String getRequiredStringInput(String prompt) {
        String input;
        do {
            input = getStringInput(prompt);
            if (input.isEmpty()) {
                System.out.println(
                    "This field is required. Please enter a value."
                );
            }
        } while (input.isEmpty());
        return input;
    }

    protected LocalDate getDateInput(String prompt) {
        while (true) {
            String input = getRequiredStringInput(prompt);
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println(
                    "Invalid date format. Please use yyyy-MM-dd format."
                );
            }
        }
    }

    public abstract void start();
}
