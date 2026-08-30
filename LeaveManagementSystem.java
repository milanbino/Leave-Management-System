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
     * Seeds initial sample data (currently empty so users must sign up first).
     */
    private void seedSampleData() {
        // Starts empty - employees and managers sign up via registration portal
    }

    /**
     * Registers a new regular Employee.
     */
    public Employee registerEmployee(int id, String name, String email, String dept, String password) {
        if (findEmployeeById(id) != null) {
            System.out.println("Error: Employee ID " + id + " already exists!");
            return null;
        }
        Employee emp = new Employee(id, name, email, dept, password);
        employees.add(emp);
        System.out.println("Employee " + name + " registered successfully with ID " + id + "!");
        return emp;
    }

    /**
     * Registers a new Manager.
     */
    public Manager registerManager(int id, String name, String email, String dept, String password) {
        if (findEmployeeById(id) != null) {
            System.out.println("Error: Manager ID " + id + " already exists!");
            return null;
        }
        Manager mgr = new Manager(id, name, email, dept, password);
        employees.add(mgr);
        System.out.println("Manager " + name + " registered successfully with ID " + id + "!");
        return mgr;
    }

    /**
     * Authenticates an employee or manager with ID and Password.
     */
    public Employee authenticateUser(int id, String password) {
        Employee emp = findEmployeeById(id);
        if (emp == null) {
            System.out.println("Error: Account ID " + id + " not found!");
            return null;
        }
        if (!emp.verifyPassword(password)) {
            System.out.println("Error: Incorrect password for ID " + id + "!");
            return null;
        }
        return emp;
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
