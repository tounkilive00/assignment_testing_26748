import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

public class Main {
    private static final int EXIT_OPTION = 6;

    private static final Scanner scanner = new Scanner(System.in);
    private static final OrganizationSystem system = new OrganizationSystem();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String line = readLine();
            if (line == null) {          
                System.out.println();
                break;
            }

            Integer choice = InputValidator.parseMenuChoice(line, EXIT_OPTION);
            if (choice == null) {
                System.out.println("Invalid menu option. Please enter a number from 1 to " + EXIT_OPTION + ".");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> addEmployee();
                    case 2 -> findEmployee();
                    case 3 -> processPayment();
                    case 4 -> listEmployees();
                    case 5 -> System.out.println(system.generateReport());
                    case 6 -> running = false;
                    default -> System.out.println("Invalid menu option.");
                }
            } catch (IllegalArgumentException e) {
                // Safety net only: every input is validated before it reaches the model.
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        System.out.println("Application closed.");
    }

    private static void printMenu() {
        System.out.println("\n===== ORGANIZATION MANAGEMENT SYSTEM =====");
        System.out.println("1. Add employee");
        System.out.println("2. Find employee");
        System.out.println("3. Process employee payment");
        System.out.println("4. List employees");
        System.out.println("5. Generate report");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    // ---------------------------------------------------------------- actions

    private static void addEmployee() {
        System.out.println("(type 'cancel' at any prompt to go back to the menu)");

        Integer id = promptNewId();
        if (id == null) return;

        String name = promptValid("Enter employee name: ", InputValidator::normalizeName, InputValidator.NAME_RULES);
        if (name == null) return;

        Double salary = promptValid("Enter monthly salary: ", InputValidator::parseSalary, InputValidator.SALARY_RULES);
        if (salary == null) return;

        boolean added = system.addEmployee(new Employee(id, name, salary));
        System.out.println(added ? "Employee added successfully." : "Employee ID already exists.");
    }

    private static void findEmployee() {
        if (system.getEmployeeCount() == 0) {
            System.out.println("No employees registered yet.");
            return;
        }

        Integer id = promptValid("Enter employee ID (or 'cancel'): ", InputValidator::parseId, InputValidator.ID_RULES);
        if (id == null) return;

        Employee employee = system.findEmployee(id);
        System.out.println(employee == null ? "Employee not found." : employee);
    }

    private static void processPayment() {
        if (system.getEmployeeCount() == 0) {
            System.out.println("No employees registered yet.");
            return;
        }

        Integer id = promptValid("Enter employee ID (or 'cancel'): ", InputValidator::parseId, InputValidator.ID_RULES);
        if (id == null) return;

        Employee employee = system.findEmployee(id);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }
        System.out.println("Employee: " + employee.getName());

        Integer days = promptValid("Enter days worked (" + InputValidator.MIN_DAYS + "-" + InputValidator.MAX_DAYS
                + ", or 'cancel'): ", InputValidator::parseDays, InputValidator.DAYS_RULES);
        if (days == null) return;

        double payment = system.calculatePayment(id, days);
        System.out.println(String.format(Locale.US, "Payment to process: %.2f", payment));
    }

    private static void listEmployees() {
        System.out.println(system.generateEmployeeList());
    }



    private static Integer promptNewId() {
        while (true) {
            Integer id = promptValid("Enter employee ID: ", InputValidator::parseId, InputValidator.ID_RULES);
            if (id == null) return null;
            if (system.findEmployee(id) == null) return id;
            System.out.println("Employee ID " + id + " already exists. Please choose another ID.");
        }
    }

    
    private static <T> T promptValid(String prompt, Function<String, T> parser, String rules) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            if (line == null) return null;
            if (line.trim().equalsIgnoreCase("cancel")) {
                System.out.println("Cancelled.");
                return null;
            }

            T value = parser.apply(line);
            if (value != null) return value;
            System.out.println("Invalid input. " + rules);
        }
    }

    private static String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }
}
