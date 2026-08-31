import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * LeaveManagementSystem Class
 * Main controller class managing employees, managers, leave requests,
 * and approval workflows with FILE PERSISTENCE (employees.txt & requests.txt).
 */
public class LeaveManagementSystem {
    private List<Employee> employees;
    private List<LeaveRequest> leaveRequests;
    private LeaveCalendar leaveCalendar;
    private int nextRequestId;

    private static final String EMPLOYEES_FILE = "employees.txt";
    private static final String REQUESTS_FILE = "requests.txt";

    public LeaveManagementSystem() {
        this.employees = new ArrayList<>();
        this.leaveRequests = new ArrayList<>();
        this.leaveCalendar = new LeaveCalendar();
        this.nextRequestId = 1001; // Request IDs start from 1001

        // Load existing saved data from local text files
        loadDataFromFiles();
    }

    /**
     * Loads registered users and leave requests from local text files.
     */
    private void loadDataFromFiles() {
        // 1. Load Employees & Managers
        File empFile = new File(EMPLOYEES_FILE);
        if (empFile.exists()) {
            try (Scanner fileScanner = new Scanner(empFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();
                    if (line.isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length == 6) {
                        String role = parts[0];
                        int id = Integer.parseInt(parts[1]);
                        String name = parts[2];
                        String email = parts[3];
                        String dept = parts[4];
                        String password = parts[5];

                        if ("Manager".equalsIgnoreCase(role)) {
                            employees.add(new Manager(id, name, email, dept, password));
                        } else {
                            employees.add(new Employee(id, name, email, dept, password));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notice: Could not load " + EMPLOYEES_FILE + " (" + e.getMessage() + ")");
            }
        }

        // 2. Load Leave Requests
        File reqFile = new File(REQUESTS_FILE);
        if (reqFile.exists()) {
            try (Scanner fileScanner = new Scanner(reqFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();
                    if (line.isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length == 7) {
                        int reqId = Integer.parseInt(parts[0]);
                        int empId = Integer.parseInt(parts[1]);
                        LeaveType type = LeaveType.valueOf(parts[2]);
                        LocalDate startDate = LocalDate.parse(parts[3]);
                        LocalDate endDate = LocalDate.parse(parts[4]);
                        String reason = parts[5];
                        LeaveStatus status = LeaveStatus.valueOf(parts[6]);

                        Employee emp = findEmployeeById(empId);
                        if (emp != null) {
                            LeaveRequest req = new LeaveRequest(reqId, emp, type, startDate, endDate, reason);
                            req.setStatus(status);
                            leaveRequests.add(req);
                            if (reqId >= nextRequestId) {
                                nextRequestId = reqId + 1;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notice: Could not load " + REQUESTS_FILE + " (" + e.getMessage() + ")");
            }
        }
    }

    /**
     * Saves all active employees and managers to employees.txt
     */
    public void saveEmployeesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_FILE))) {
            for (Employee emp : employees) {
                writer.println(emp.getRole() + "|" + emp.getEmployeeId() + "|" + emp.getName() + "|" + emp.getEmail() + "|" + emp.getDepartment() + "|" + emp.getPassword());
            }
        } catch (IOException e) {
            System.out.println("Error saving employees: " + e.getMessage());
        }
    }

    /**
     * Saves all leave requests to requests.txt
     */
    public void saveRequestsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REQUESTS_FILE))) {
            for (LeaveRequest req : leaveRequests) {
                writer.println(req.getRequestId() + "|" + req.getEmployee().getEmployeeId() + "|" + req.getLeaveType() + "|" + req.getStartDate() + "|" + req.getEndDate() + "|" + req.getReason() + "|" + req.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error saving requests: " + e.getMessage());
        }
    }

    /**
     * Registers a new regular Employee and saves to file.
     */
    public Employee registerEmployee(int id, String name, String email, String dept, String password) {
        if (findEmployeeById(id) != null) {
            System.out.println("Error: Employee ID " + id + " already exists!");
            return null;
        }
        Employee emp = new Employee(id, name, email, dept, password);
        employees.add(emp);
        saveEmployeesToFile(); // Persist to file
        System.out.println("Employee " + name + " registered successfully with ID " + id + "!");
        return emp;
    }

    /**
     * Registers a new Manager and saves to file.
     */
    public Manager registerManager(int id, String name, String email, String dept, String password) {
        if (findEmployeeById(id) != null) {
            System.out.println("Error: Manager ID " + id + " already exists!");
            return null;
        }
        Manager mgr = new Manager(id, name, email, dept, password);
        employees.add(mgr);
        saveEmployeesToFile(); // Persist to file
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
     * Submits a new leave request after basic validations and saves to file.
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
        saveRequestsToFile(); // Persist to file
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
     * Approves a leave request using Manager delegation and saves state to file.
     */
    public boolean approveRequest(int requestId, Manager manager) {
        LeaveRequest req = findRequestById(requestId);
        if (req == null) {
            System.out.println("Error: Request ID " + requestId + " not found!");
            return false;
        }

        boolean success = manager.approveLeave(req);
        if (success) {
            saveRequestsToFile(); // Persist updated status
        }
        return success;
    }

    /**
     * Rejects a leave request using Manager delegation and saves state to file.
     */
    public boolean rejectRequest(int requestId, Manager manager) {
        LeaveRequest req = findRequestById(requestId);
        if (req == null) {
            System.out.println("Error: Request ID " + requestId + " not found!");
            return false;
        }

        boolean success = manager.rejectLeave(req);
        if (success) {
            saveRequestsToFile(); // Persist updated status
        }
        return success;
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
