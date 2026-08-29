/**
 * Employee Class
 * Base class representing a regular employee in the company.
 * Demonstrates ENCAPSULATION using private fields and public getters/setters.
 */
public class Employee {
    // Private attributes (Encapsulation)
    private int employeeId;
    private String name;
    private String email;
    private String department;

    // Constructor to initialize an Employee object
    public Employee(int employeeId, String name, String email, String department) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Returns the role of the employee.
     * Demonstrates POLYMORPHISM: Overridden by the child class Manager.
     */
    public String getRole() {
        return "Employee";
    }

    // Display employee basic information
    public void displayDetails() {
        System.out.println("ID: " + employeeId + " | Name: " + name + " | Department: " + department + " | Role: " + getRole());
    }
}
