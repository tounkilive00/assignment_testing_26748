# Organization Management Console App

Java console application implementing the homework scenario:
- Employee management (add, find, **list**)
- Payment/payroll processing
- Reporting

There is intentionally **no delete option**: employees can be added and viewed, never removed.

## Menu
1. Add employee
2. Find employee
3. Process employee payment
4. List employees
5. Generate report
6. Exit

## Input validation
The UI never crashes on bad input; it explains the rule and asks again. Type `cancel` at any prompt to return to the menu.
All rules live in `InputValidator.java`:

| Field | Rule |
|-------|------|
| Menu option | whole number 1-6 |
| Employee ID | digits only, 1 to 999999999, must be unique when adding |
| Name | 2-50 chars, starts with a letter, letters/spaces/apostrophes/hyphens/dots only |
| Salary | 0 to 1000000000, digits with an optional dot and at most 2 decimals (no letters, NaN, exponents or negatives) |
| Days worked | whole number 0-31 |

Closing the input stream (Ctrl+D / Ctrl+Z) exits cleanly instead of throwing.

## Requirements
- Java JDK 17 or later

## Run
```bash
javac -d out *.java
java -cp out Main
```

## Automated checks
```bash
java -cp out TestConsole
```
