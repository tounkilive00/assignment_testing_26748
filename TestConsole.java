import java.util.List;

/**
 * Plain checks (no -ea flag needed). Exits with status 1 if any check fails.
 */
public class TestConsole {
    private static int failures = 0;

    private static void check(boolean condition, String label) {
        if (!condition) {
            failures++;
            System.out.println("FAILED: " + label);
        }
    }

    public static void main(String[] args) {
        OrganizationSystem system = new OrganizationSystem();

        // --- system behaviour
        check(system.addEmployee(new Employee(1, "Alice", 3000)), "add first employee");
        check(!system.addEmployee(new Employee(1, "Duplicate", 4000)), "reject duplicate ID");
        check(!system.addEmployee(null), "reject null employee");
        check(system.findEmployee(1) != null, "find existing employee");
        check(system.findEmployee(99) == null, "unknown employee returns null");
        check(Math.abs(system.calculatePayment(1, 15) - 1500.0) < 0.001, "payment for 15 days");
        check(system.getEmployeeCount() == 1, "employee count");

        // --- employee list
        check(new OrganizationSystem().generateEmployeeList().equals("No employees registered."), "empty list message");
        system.addEmployee(new Employee(2, "Jean-Luc O'Brien", 2500.5));
        List<Employee> list = system.getEmployees();
        check(list.size() == 2 && list.get(0).getId() == 1 && list.get(1).getId() == 2, "list keeps insertion order");
        check(system.generateEmployeeList().contains("Jean-Luc O'Brien"), "list table shows names");
        try {
            list.remove(0);
            check(false, "returned list must be read-only");
        } catch (UnsupportedOperationException expected) {
            check(true, "returned list is read-only");
        }
        check(system.getEmployeeCount() == 2, "no employee removed");

        // --- validators: valid input
        check(InputValidator.parseId(" 42 ") != null && InputValidator.parseId(" 42 ") == 42, "valid ID");
        check("Marie Curie".equals(InputValidator.normalizeName("  Marie   Curie ")), "name is normalised");
        check("Zoé Àlvarez".equals(InputValidator.normalizeName("Zoé Àlvarez")), "accented name accepted");
        check(InputValidator.parseSalary("1500.50") != null && InputValidator.parseSalary("1500.50") == 1500.50, "valid salary");
        check(InputValidator.parseSalary("0") != null, "zero salary accepted");
        check(InputValidator.parseDays("31") != null && InputValidator.parseDays("0") != null, "days bounds accepted");
        check(InputValidator.parseMenuChoice("6", 6) != null, "menu bound accepted");

        // --- validators: invalid input must return null, never throw
        String[] badIds = {null, "", " ", "0", "-1", "abc", "1.5", "1e3", "1000000000", "99999999999999999999", "١٢٣"};
        for (String bad : badIds) check(InputValidator.parseId(bad) == null, "bad ID rejected: " + bad);

        String[] badNames = {null, "", "   ", "A", "R2D2", "<script>", "Bob;", "-Bob", "x".repeat(51)};
        for (String bad : badNames) check(InputValidator.normalizeName(bad) == null, "bad name rejected: " + bad);

        String[] badSalaries = {null, "", "abc", "-5", "NaN", "Infinity", "1e5", "1,500", "12.345", "1000000001", "99999999999", ".5"};
        for (String bad : badSalaries) check(InputValidator.parseSalary(bad) == null, "bad salary rejected: " + bad);

        String[] badDays = {null, "", "-1", "32", "100", "abc", "1.5"};
        for (String bad : badDays) check(InputValidator.parseDays(bad) == null, "bad days rejected: " + bad);

        check(InputValidator.parseMenuChoice("0", 6) == null, "menu 0 rejected");
        check(InputValidator.parseMenuChoice("7", 6) == null, "menu 7 rejected");
        check(InputValidator.parseMenuChoice("x", 6) == null, "menu letter rejected");

        // --- model safety net still throws for programming mistakes
        expectIllegalArgument(() -> new Employee(0, "Bob", 1), "id 0");
        expectIllegalArgument(() -> new Employee(1, " ", 1), "blank name");
        expectIllegalArgument(() -> new Employee(1, "Bob", -1), "negative salary");
        expectIllegalArgument(() -> new Employee(1, "Bob", Double.NaN), "NaN salary");
        expectIllegalArgument(() -> system.calculatePayment(1, 32), "32 days");
        expectIllegalArgument(() -> system.calculatePayment(99, 5), "unknown employee payment");

        if (failures == 0) {
            System.out.println("All automated checks passed.");
        } else {
            System.out.println(failures + " check(s) failed.");
            System.exit(1);
        }
    }

    private static void expectIllegalArgument(Runnable action, String label) {
        try {
            action.run();
            check(false, "should throw IllegalArgumentException: " + label);
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }
}
