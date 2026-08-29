import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * LeaveManagementSystem Class
 * Main controller class that manages employees, managers, leave requests,
 * and approval workflows using Java collections (ArrayList).
 */
public class LeaveManagementSystem {
    private List<Employee> employees;
    private List<LeaveRequest> leaveRequests;
    private LeaveCalendar leaveCalendar;
    private int nextRequestId;

    public LeaveManagementSystem() {
        this.employees = new ArrayList<>();
        this.leaveRequests = new ArrayList<>();
        this.leaveCalendar = new LeaveCalendar();
        this.nextRequestId = 1001; // Request IDs start from 1001

        // Seed initial sample data for easy testing
        seedSampleData();
    }

    /**
     * Seeds initial sample employees and manager.
     */
    private void seedSampleData() {
        // Sample Employees
        employees.add(new Employee(101, "Esther", "esther@company.com", "IT"));
        employees.add(new Employee(102, "Anu", "anu@company.com", "IT"));
        employees.add(new Employee(103, "Rahul", "rahul@company.com", "IT"));
        employees.add(new Employee(104, "David", "david@company.com", "HR"));
        employees.add(new Employee(105, "Sophia", "sophia@company.com", "Finance"));

        // Sample Managers (Demonstrates Inheritance & Polymorphism)
        employees.add(new Manager(201, "John", "john@company.com", "IT"));
        employees.add(new Manager(202, "Sarah", "sarah@company.com", "HR"));
    }

    /**
     * Finds an employee or manager by ID.
     */
    public Employee findEmployeeById(int employeeId) {
        for (Employee emp : employees) {
            if (emp.getEmployeeId() == employeeId) {
                return emp;
            }
        }
        return null;
    }

    /**
     * Submits a new leave request after basic validations.
     */
    public LeaveRequest submitLeaveRequest(int employeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate, String reason) {
        Employee emp = findEmployeeById(employeeId);
        if (emp == null) {
            System.out.println("Error: Employee ID " + employeeId + " does not exist!");
            return null;
        }

        if (endDate.isBefore(startDate)) {
            System.out.println("Error: End date cannot be before start date!");
            return null;
        }

        if (reason == null || reason.trim().isEmpty()) {
            System.out.println("Error: Leave reason cannot be empty!");
            return null;
        }

        LeaveRequest newRequest = new LeaveRequest(nextRequestId++, emp, leaveType, startDate, endDate, reason.trim());
        leaveRequests.add(newRequest);
        return newRequest;
    }

    /**
     * Returns a list of all requests with PENDING status.
     */
    public List<LeaveRequest> getPendingRequests() {
        List<LeaveRequest> pending = new ArrayList<>();
        for (LeaveRequest req : leaveRequests) {
            if (req.getStatus() == LeaveStatus.PENDING) {
                pending.add(req);
            }
        }
        return pending;
    }

    /**
     * Returns leave requests submitted by a specific employee.
     */
    public List<LeaveRequest> getEmployeeRequests(int employeeId) {
        List<LeaveRequest> empRequests = new ArrayList<>();
        for (LeaveRequest req : leaveRequests) {
            if (req.getEmployee().getEmployeeId() == employeeId) {
                empRequests.add(req);
            }
        }
        return empRequests;
    }

    /**
     * Finds a leave request by its Request ID.
     */
    public LeaveRequest findRequestById(int requestId) {
        for (LeaveRequest req : leaveRequests) {
            if (req.getRequestId() == requestId) {
                return req;
            }
        }
        return null;
    }

    /**
     * Approves a leave request using Manager delegation.
     */
    public boolean approveRequest(int requestId, Manager manager) {
        LeaveRequest req = findRequestById(requestId);
        if (req == null) {
            System.out.println("Error: Request ID " + requestId + " not found!");
            return false;
        }

        // Delegate to Manager (LeaveApprover implementation)
        return manager.approveLeave(req);
    }

    /**
     * Rejects a leave request using Manager delegation.
     */
    public boolean rejectRequest(int requestId, Manager manager) {
        LeaveRequest req = findRequestById(requestId);
        if (req == null) {
            System.out.println("Error: Request ID " + requestId + " not found!");
            return false;
        }

        // Delegate to Manager (LeaveApprover implementation)
        return manager.rejectLeave(req);
    }

    /**
     * Displays team-wide calendar.
     */
    public void displayTeamCalendar() {
        leaveCalendar.displayTeamCalendar(leaveRequests);
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
