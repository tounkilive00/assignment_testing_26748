import java.util.Locale;

public class Employee {
    private final int id;
    private final String name;
    private final double salary;

   
    public Employee(int id, String name, double salary) {
        if (id <= 0) throw new IllegalArgumentException("ID must be positive.");
        if (id > InputValidator.MAX_ID) {
            throw new IllegalArgumentException("ID cannot exceed " + InputValidator.MAX_ID + ".");
        }

        String cleanName = InputValidator.normalizeName(name);
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required.");
        if (cleanName == null) throw new IllegalArgumentException(InputValidator.NAME_RULES);

        if (Double.isNaN(salary) || Double.isInfinite(salary)) {
            throw new IllegalArgumentException("Salary must be a valid number.");
        }
        if (salary < 90000) throw new IllegalArgumentException("Salary cannot be negative or less than 90000");
        if (salary > InputValidator.MAX_SALARY) {
            throw new IllegalArgumentException("Salary cannot exceed " + (long) InputValidator.MAX_SALARY + ".");
        }

        this.id = id;
        this.name = cleanName;
        this.salary = salary;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Salary: " + String.format(Locale.US, "%.2f", salary);
    }
}
