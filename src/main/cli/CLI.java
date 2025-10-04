package main.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Abstract base class for all CLI (Command Line Interface) implementations.
 * <p>
 * This class provides common input validation utilities and follows the DRY
 * (Don't Repeat Yourself) principle by centralizing input validation logic
 * that is shared across multiple CLI classes. All concrete CLI classes should
 * extend this base class to inherit the validation functionality.
 * </p>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 * <li>Integer input validation with error handling</li>
 * <li>Double/decimal input validation</li>
 * <li>String input handling (optional and required)</li>
 * <li>Date input validation in yyyy-MM-dd format</li>
 * <li>Automatic retry on invalid input</li>
 * <li>User-friendly error messages</li>
 * </ul>
 * 
 * <h3>Usage:</h3>
 * 
 * <pre>
 * public class MyCLI extends CLI {
 *     public void start() {
 *         int age = getIntInput("Enter your age: ");
 *         String name = getRequiredStringInput("Enter your name: ");
 *         // ... use the inputs
 *     }
 * }
 * </pre>
 */
public abstract class CLI {

    /**
     * Scanner instance for reading user input from console (shared across all
     * methods)
     */
    protected final Scanner scanner;

    /**
     * Standard date formatter for parsing and displaying dates in ISO format
     * (yyyy-MM-dd)
     */
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Protected constructor initializing the Scanner for user input.
     * <p>
     * This constructor is called by subclasses to initialize the shared
     * Scanner instance used for all input operations.
     * </p>
     */
    protected CLI() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads and validates integer input from the user with automatic retry on
     * error.
     * <p>
     * This method continuously prompts the user until a valid integer is entered.
     * It handles NumberFormatException gracefully by displaying an error message
     * and re-prompting the user. Leading and trailing whitespace is automatically
     * removed.
     * </p>
     * 
     * @param prompt the message to display when asking for input (e.g., "Enter your
     *               age: ")
     * @return the validated integer input from the user
     */
    protected int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                // Parse input, trimming whitespace first
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                // Invalid input - inform user and retry
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Reads and validates double (decimal) input from the user with automatic retry
     * on error.
     * <p>
     * This method continuously prompts the user until a valid decimal number is
     * entered.
     * It handles NumberFormatException gracefully by displaying an error message
     * and re-prompting the user. Useful for monetary amounts, measurements, etc.
     * </p>
     * 
     * @param prompt the message to display when asking for input (e.g., "Enter
     *               billing amount: ")
     * @return the validated double input from the user
     */
    protected double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                // Parse input as double, trimming whitespace first
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                // Invalid input - inform user and retry
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Reads string input from the user without validation.
     * <p>
     * This method reads a line of text from the user and trims leading/trailing
     * whitespace. The input can be empty. For required fields, use
     * {@link #getRequiredStringInput(String)} instead.
     * </p>
     * 
     * @param prompt the message to display when asking for input
     * @return the trimmed string input (may be empty)
     * @see #getRequiredStringInput(String) for required fields
     */
    protected String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads and validates required (non-empty) string input from the user.
     * <p>
     * This method ensures that the user provides a non-empty value by continuously
     * prompting until valid input is received. Empty strings (after trimming) are
     * rejected with an error message. Use this for mandatory fields.
     * </p>
     * 
     * @param prompt the message to display when asking for input
     * @return the validated non-empty string input
     * @see #getStringInput(String) for optional fields
     */
    protected String getRequiredStringInput(String prompt) {
        String input;
        do {
            input = getStringInput(prompt);
            if (input.isEmpty()) {
                // Inform user that field is required and retry
                System.out.println("This field is required. Please enter a value.");
            }
        } while (input.isEmpty());
        return input;
    }

    /**
     * Reads and validates date input in ISO format (yyyy-MM-dd).
     * <p>
     * This method prompts the user for a date in ISO format (e.g., "2025-10-03")
     * and continuously retries until a valid date is entered. It handles parsing
     * errors gracefully by displaying an error message and re-prompting. The field
     * is required - empty input is not accepted.
     * </p>
     * 
     * <h3>Valid Date Examples:</h3>
     * <ul>
     * <li>2025-10-03</li>
     * <li>1990-01-15</li>
     * <li>2000-12-31</li>
     * </ul>
     * 
     * @param prompt the message to display when asking for input (should mention
     *               format)
     * @return the validated LocalDate object representing the user's input
     */
    protected LocalDate getDateInput(String prompt) {
        while (true) {
            String input = getRequiredStringInput(prompt);
            try {
                // Parse date string using standard formatter
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                // Invalid date format - inform user and retry
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }

    /**
     * Abstract method to be implemented by subclasses to start their specific CLI
     * interface.
     * <p>
     * Each concrete CLI class must implement this method to define its main
     * interaction loop.
     * This method should display menus, handle user choices, and delegate to
     * appropriate
     * action methods.
     * </p>
     */
    public abstract void start();
}
