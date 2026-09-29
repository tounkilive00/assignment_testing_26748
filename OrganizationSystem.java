import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Holds the employees. There is intentionally no way to remove an employee.
 */
public class OrganizationSystem {
    private final List<Employee> employees = new ArrayList<>();

    public boolean addEmployee(Employee employee) {
        if (employee == null) return false;
        if (findEmployee(employee.getId()) != null) return false;
        employees.add(employee);
        return true;
    }

    public Employee findEmployee(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    /** Read-only snapshot of the employees, in the order they were added. */
    public List<Employee> getEmployees() {
        return List.copyOf(employees);
    }

    public double calculatePayment(int employeeId, int daysWorked) {
        Employee employee = findEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found.");
        }
        if (daysWorked < InputValidator.MIN_DAYS || daysWorked > InputValidator.MAX_DAYS) {
            throw new IllegalArgumentException("Days worked must be between 0 and 31.");
        }

        double dailyRate = employee.getSalary() / 30.0;
        return dailyRate * daysWorked;
    }

    /** Table with one row per employee. */
    public String generateEmployeeList() {
        if (employees.isEmpty()) {
            return "No employees registered.";
        }

        int idWidth = 9;      // up to 9 digits
        int salaryWidth = 14; // up to 1000000000.00
        int nameWidth = "Name".length();
        for (Employee employee : employees) {
            nameWidth = Math.max(nameWidth, employee.getName().length());
        }

        String rowFormat = "%-" + idWidth + "s  %-" + nameWidth + "s  %" + salaryWidth + "s%n";
        String separator = "-".repeat(idWidth + 2 + nameWidth + 2 + salaryWidth);

        StringBuilder list = new StringBuilder();
        list.append("\n===== EMPLOYEE LIST =====\n");
        list.append(String.format(Locale.US, rowFormat, "ID", "Name", "Salary"));
        list.append(separator).append("\n");
        for (Employee employee : employees) {
            list.append(String.format(Locale.US, rowFormat,
                    employee.getId(), employee.getName(),
                    String.format(Locale.US, "%.2f", employee.getSalary())));
        }
        list.append(separator).append("\n");
        list.append("Total employees: ").append(employees.size()).append("\n");
        return list.toString();
    }

    public String generateReport() {
        if (employees.isEmpty()) {
            return "No employees registered.";
        }

        StringBuilder report = new StringBuilder();
        report.append("\n===== ORGANIZATION REPORT =====\n");
        report.append("Total employees: ").append(employees.size()).append("\n");

        double totalSalary = 0;
        for (Employee employee : employees) {
            report.append(employee).append("\n");
            totalSalary += employee.getSalary();
        }

        report.append("Total monthly salaries: ")
              .append(String.format(Locale.US, "%.2f", totalSalary))
              .append("\n");
        report.append("===============================\n");
        return report.toString();
    }

    public int getEmployeeCount() {
        return employees.size();
    }
}
