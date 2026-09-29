import java.util.regex.Pattern;


public final class InputValidator {

    public static final int MAX_ID = 999_999_999;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;
    public static final double MAX_SALARY = 1_000_000_000.0;
    public static final int MIN_DAYS = 0;
    public static final int MAX_DAYS = 31;

    public static final String ID_RULES =
            "ID must be a whole number from 1 to " + MAX_ID + " (digits only).";
    public static final String NAME_RULES =
            "Name must be " + MIN_NAME_LENGTH + "-" + MAX_NAME_LENGTH
            + " characters, start with a letter and contain only letters, spaces, apostrophes, hyphens or dots.";
    public static final String SALARY_RULES =
            "Salary must be a number from 0 to 1000000000 with at most 2 decimals (example: 1500.50).";
    public static final String DAYS_RULES =
            "Days worked must be a whole number from " + MIN_DAYS + " to " + MAX_DAYS + ".";

    // In Java regex, \d matches ASCII digits only, so exotic digits are rejected too.
    private static final Pattern ID_PATTERN = Pattern.compile("\\d{1,9}");
    private static final Pattern NAME_PATTERN = Pattern.compile("\\p{L}[\\p{L} .'-]*");
    private static final Pattern SALARY_PATTERN = Pattern.compile("\\d{1,10}(\\.\\d{1,2})?");
    private static final Pattern DAYS_PATTERN = Pattern.compile("\\d{1,2}");
    private static final Pattern MENU_PATTERN = Pattern.compile("\\d{1,2}");

    private InputValidator() { }

 
    public static Integer parseId(String raw) {
        String text = clean(raw);
        if (text == null || !ID_PATTERN.matcher(text).matches()) return null;
        int id = Integer.parseInt(text); // safe: at most 9 digits
        return (id >= 1 && id <= MAX_ID) ? id : null;
    }

    /** @return the trimmed name with single spaces, or null if the name is not valid. */
    public static String normalizeName(String raw) {
        if (raw == null) return null;
        String name = raw.trim().replaceAll("\\s+", " ");
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) return null;
        return NAME_PATTERN.matcher(name).matches() ? name : null;
    }

    /** @return the salary (0..MAX_SALARY) or null. Rejects letters, NaN, Infinity, exponents, negatives. */
    public static Double parseSalary(String raw) {
        String text = clean(raw);
        if (text == null || !SALARY_PATTERN.matcher(text).matches()) return null;
        double salary = Double.parseDouble(text); // safe: matches digits[.digits]
        return (salary >= 0 && salary <= MAX_SALARY) ? salary : null;
    }

    /** @return days worked (0..31) or null. */
    public static Integer parseDays(String raw) {
        String text = clean(raw);
        if (text == null || !DAYS_PATTERN.matcher(text).matches()) return null;
        int days = Integer.parseInt(text);
        return (days >= MIN_DAYS && days <= MAX_DAYS) ? days : null;
    }

    /** @return the menu option (1..maxOption) or null. */
    public static Integer parseMenuChoice(String raw, int maxOption) {
        String text = clean(raw);
        if (text == null || !MENU_PATTERN.matcher(text).matches()) return null;
        int choice = Integer.parseInt(text);
        return (choice >= 1 && choice <= maxOption) ? choice : null;
    }

    private static String clean(String raw) {
        return raw == null ? null : raw.trim();
    }
}
