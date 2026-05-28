import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Scanner;

public class IOHelper {

    /** Single Scanner instance shared by all methods in this class. */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prints the given object followed by a newline.
     *
     * @param o the object to print
     */
    public static void println(Object o){
        System.out.println(o);
    }

    /**
     * Prints the given object without adding any extra formatting.
     *
     * @param o the object to print
     */
    public static void print(Object o){
        System.out.print(o);
    }

    /**
     * Prints a formatted string using the same rules as System.out.printf.
     *
     * @param format a format string
     * @param args   values referenced by the format specifiers in the format string
     */
    public static void printf(String format, Object... args){
        System.out.printf(format, args);
    }

    /**
     * Reads an integer from user input.
     * Automatically handles invalid input by re-prompting.
     *
     * @param prompt the message to display to the user
     * @return the integer entered by the user
     *
     * Example:
     *     int count = readInt("How many? ");
     */
    public static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            String garbage = scanner.next(); // consume the invalid input
            System.out.print(garbage + " is invalid input. \n" + prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline
        return value;
    }

    /**
     * Reads an integer within a specified range [min, max] inclusive.
     * Automatically re-prompts if the value is out of range or invalid.
     *
     * @param prompt the message to display to the user
     * @param min    the minimum acceptable value (inclusive)
     * @param max    the maximum acceptable value (inclusive)
     * @return the integer entered by the user, guaranteed to be in [min, max]
     *
     * Example:
     *     int age = readInt("Enter age: ", 0, 120);
     */
    public static int readInt(String prompt, int min, int max) {
        int value;
        do {
            value = readInt(prompt);
            if (value < min || value > max) {
                System.out.println("Value must be between " + min + " and " + max);
            }
        } while (value < min || value > max);
        return value;
    }

    /**
     * Reads an integer within a specified range [min, max] inclusive,
     * or allows a sentinel value to exit early.
     *
     * If the user enters the sentinel value, it is returned immediately
     * without range validation.
     * Automatically re-prompts if the value is invalid or out of range
     * (and not equal to the sentinel).
     *
     * @param prompt   the message to display to the user
     * @param min      the minimum acceptable value (inclusive)
     * @param max      the maximum acceptable value (inclusive)
     * @param sentinel a special value that bypasses range checking
     *                 (commonly used to quit, such as -1)
     * @return the integer entered by the user, guaranteed to be in
     *         [min, max] or equal to sentinel
     *
     * Example:
     *     int score = readInt("Enter score (-1 to quit): ", 0, 100, -1);
     *     if (score == -1) {
     *         System.out.println("Exiting...");
     *     }
     */
    public static int readInt(String prompt, int min, int max, int sentinel) {
        int value;
        do {
            value = readInt(prompt);

            if (value != sentinel && (value < min || value > max)) {
                System.out.println("Value must be between " + min +
                        " and " + max +
                        " (or " + sentinel + " to quit).");
            }

        } while (value != sentinel && (value < min || value > max));

        return value;
    }
    /**
     * Reads a double from user input.
     * Automatically handles invalid input by re-prompting.
     *
     * @param prompt the message to display to the user
     * @return the double entered by the user
     *
     * Example:
     *     double price = readDouble("Enter price: $");
     */
    public static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            String garbage = scanner.next(); // consume the invalid input
            System.out.print(garbage + " is invalid input. \n" + prompt);
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // consume leftover newline
        return value;
    }

    /**
     * Reads a double within a specified range [min, max] inclusive.
     * Automatically re-prompts if the value is out of range or invalid.
     *
     * @param prompt the message to display to the user
     * @param min    the minimum acceptable value (inclusive)
     * @param max    the maximum acceptable value (inclusive)
     * @return the double entered by the user, guaranteed to be in [min, max]
     *
     * Example:
     *     double temp = readDouble("Enter temp: ", -50.0, 150.0);
     */
    public static double readDouble(String prompt, double min, double max) {
        double value;
        do {
            value = readDouble(prompt);
            if (value < min || value > max) {
                System.out.println("Value must be between " + min + " and " + max);
            }
        } while (value < min || value > max);
        return value;
    }

    /**
     * Prompts the user to enter a string and ensures the input
     * matches one of the allowed options.
     *
     * <p>The method will continue prompting until the user
     * enters one of the specified options. Comparison is
     * case-insensitive.</p>
     *
     * <p>Example:</p>
     * <pre>
     *     String answer = readString("Continue", "yes", "no", "maybe");
     * </pre>
     *
     * @param prompt  the message displayed to the user
     * @param options the list of acceptable responses
     * @return the valid string entered by the user
     */
    public static String readLine(String prompt, String... options) {

        String input;
        boolean valid;

        do {
            System.out.print(prompt + ": ");
            input = scanner.nextLine();
            valid = false;

            for (String option : options) {
                if (option.equalsIgnoreCase(input)) {
                    valid = true;
                    input = option;   // return the canonical form
                }
            }
            if (!valid) {
                System.out.print("Invalid input. Valid options are: " + Arrays.toString(options));
            }
        } while (!valid);

        return input;
    }

    /**
     * Reads a full line of text (including spaces) from user input.
     *
     * @param prompt the message to display to the user
     * @return the complete line entered by the user (may be empty)
     *
     * Example:
     *     String fullName = readLine("Enter full name: ");
     */
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Reads a yes/no response from the user.
     * Accepts: y, n, yes, no (case-insensitive).
     * Automatically re-prompts for invalid input.
     *
     * @param prompt the question to ask the user (without "(y/n)")
     * @return true if user entered y/yes; false if user entered n/no
     *
     * Example:
     *     boolean confirm = readYesNo("Continue");
     */
    public static boolean readYesNo(String prompt) {
        System.out.print(prompt + " (y/n): ");
        String response = scanner.next().toLowerCase();
        scanner.nextLine(); // consume leftover newline

        while (!response.equals("y") && !response.equals("n") &&
                !response.equals("yes") && !response.equals("no")) {
            System.out.print("Please enter y or n. " + prompt + " (y/n): ");
            response = scanner.next().toLowerCase();
            scanner.nextLine();
        }

        return response.equals("y") || response.equals("yes");
    }

    /**
     * Reads a date from the user
     * @param  prompt to ask
     * @return
     */
    public static LocalDate readISODate(String prompt) {
        LocalDate date = null; String ds = "";
        do{
            try{
                ds = readLine(prompt + " please enter in ISO format: YYYY-MM-DD (example: 2024-03-01): ");
                date = LocalDate.parse(ds);
            }
            catch(Exception e){
                System.err.println(ds + " was not in the correct format");
            }
        }while(date == null);
        return date;
    }

    /**
     * Reads a date from the user
     * @param  prompt to ask
     * @return
     */
    public static LocalDate readDate(String prompt) {
        System.out.println(prompt);
        int year = readInt("Enter year: ", 1900, 2030);
        int month = readInt("Enter month: ", 1, 12);
        int day = readInt("Enter day: ", 1, YearMonth.of(year, month).lengthOfMonth());
        return LocalDate.of(year, month, day);
    }

    /**
     * Prints a blank line.
     */
    public static void println() {
        System.out.println();
    }

    /**
     * Reads a true/false response from the user.
     * Accepts: "true" or "false" (case-insensitive).
     * Automatically re-prompts for invalid input.
     *
     * @param prompt the question to ask the user
     * @return true if user entered "true"; false if user entered "false"
     *
     * Example:
     *     boolean flag = readBoolean("Is this correct?");
     */
    public static boolean readBoolean(String prompt) {
        String input = readLine(prompt).toLowerCase();
        while (!input.equals("true") && !input.equals("false")) {
            System.err.println(input + " is invalid. Must type \"true\" or \"false\".");
            input = readLine(prompt).toLowerCase();
        }
        return input.equals("true");
    }

    /**
     * Prompts the user to select one value from an array of Objects.
     * Comparison is case-insensitive using each object's toString().
     * Re-prompts until the user enters a matching option.
     *
     * Useful with enums:
     *     VampireForm form = (VampireForm) IOHelper.chooseFrom("Choose form:", VampireForm.values());
     *     // EdStem workaround if needed:
     *     VampireForm form = (VampireForm) IOHelper.chooseFrom("Choose form:", (Object[]) VampireForm.values());
     *
     * @param prompt  the message to display to the user
     * @param options the array of valid options (any Object — works with enums, Strings, etc.)
     * @return the matching Object from the options array
     */
    public static Object chooseFrom(String prompt, Object... options) {
        String choices = "";
        for(int i = 1; i<= options.length; i++){
            choices  += i + ": " + options[i-1].toString() + "\n";
        }
        int num = readInt(choices + "\n" + prompt, 1, options.length+1);
        return options[num-1];
    }

    public static Object chooseFromOrNull(String prompt, Object... options) {
        String choices = "";
        for(int i = 1; i<= options.length; i++){
            choices  += i + ": " + options[i-1].toString() + "\n";
        }
        choices += "-1: to return without buying";
        int num = readInt(choices + "\n" + prompt, 1, options.length+1, -1);
        Object chosen = null;
        if(num !=-1 ) chosen = options[num-1];
        return chosen;
    }


    /**
     * Prints a message in red using ANSI escape codes.
     * Note: color may not display in all terminals/IDEs.
     *
     * @param s the string to print in red
     */
    public static void printRed(String s) {
        final String ANSI_RED   = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";
        System.out.println(ANSI_RED + s + ANSI_RESET);
    }

    /**
     * Returns the index of a word in an array of options, or -1 if not found.
     * Comparison is case-insensitive.
     *
     * @param word    the word to search for
     * @param options the array of possible choices
     * @return the index of the matching option, or -1 if not found
     */
    public static int contains(String word, String... options) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equalsIgnoreCase(word)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Demonstration of IOHelper methods.
     * Run this to see examples of each method in action.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("IOHelper Demo");
        System.out.println("=============\n");

        IOHelper.println("hello there");
        double cost = 2.55;
        IOHelper.printf("The cost is:  $%.2f%n", cost);

        // Basic integer input
        int count = readInt("How many items? ");
        System.out.println("You entered: " + count + "\n");

        // Integer with range validation
        int age = readInt("Enter age: ", 0, 120);
        System.out.println("Age: " + age + "\n");

        // Double with range validation
        double price = readDouble("Enter price: $", 0.01, 999.99);
        System.out.println("Price: $" + price + "\n");

        // String input
        String name = readLine("Enter full name: ");
        System.out.println("Name: " + name + "\n");

        // Yes/no input
        boolean confirm = readYesNo("Are you sure");
        if (confirm) {
            System.out.println("You confirmed!");
        } else {
            System.out.println("You cancelled.");
        }
    }


}