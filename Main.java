import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Main Class
 * Entry point for the Employee Leave Management System application.
 * Displays interactive console menus for Employees and Managers.
 */
public class Main {
    private static LeaveManagementSystem system = new LeaveManagementSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Welcome to Employee Leave Management   ");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleEmployeeLogin();
                    break;
                case 2:
                    handleManagerLogin();
                    break;
                case 3:
                    System.out.println("\nThank you for using Employee Leave Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid choice! Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n========================================");
        System.out.println("       EMPLOYEE LEAVE MANAGEMENT        ");
        System.out.println("========================================");
        System.out.println("1. Employee Login");
        System.out.println("2. Manager Login");
        System.out.println("3. Exit");
    }

    private static void handleEmployeeLogin() {
        System.out.println("\n--- Sample Employee IDs ---");
        System.out.println("101: Esther (IT) | 102: Anu (IT) | 103: Rahul (IT) | 104: David (HR) | 105: Sophia (Finance)");
        int empId = readIntInput("Enter Employee ID: ");
        Employee emp = system.findEmployeeById(empId);

        if (emp == null) {
            System.out.println("Error: Employee ID " + empId + " not found!");
            return;
        }

        System.out.println("\nWelcome, " + emp.getName() + " (" + emp.getRole() + ")");
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n========== EMPLOYEE MENU ==========");
            System.out.println("1. Submit Leave Request");
            System.out.println("2. View My Leave Requests");
            System.out.println("3. Logout");

            int choice = readIntInput("Enter your choice: ");
            switch (choice) {
                case 1:
                    submitLeaveFlow(emp);
                    break;
                case 2:
                    viewEmployeeRequestsFlow(emp);
                    break;
                case 3:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1, 2, or 3.");
            }
        }
    }

    private static void handleManagerLogin() {
        System.out.println("\n--- Sample Manager IDs ---");
        System.out.println("201: John (IT Manager) | 202: Sarah (HR Manager)");
        int managerId = readIntInput("Enter Manager ID: ");
        Employee emp = system.findEmployeeById(managerId);

        if (emp == null || !(emp instanceof Manager)) {
            System.out.println("Error: Manager ID " + managerId + " not found or user is not a Manager!");
            return;
        }

        Manager manager = (Manager) emp; // Safe downcasting
        System.out.println("\nWelcome Manager, " + manager.getName() + "!");
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n========== MANAGER MENU ==========");
            System.out.println("1. View Pending Leave Requests");
            System.out.println("2. Approve Leave Request");
            System.out.println("3. Reject Leave Request");
            System.out.println("4. View Team Leave Calendar");
            System.out.println("5. Logout");

            int choice = readIntInput("Enter your choice: ");
            switch (choice) {
                case 1:
                    viewPendingRequestsFlow();
                    break;
                case 2:
                    approveRequestFlow(manager);
                    break;
                case 3:
                    rejectRequestFlow(manager);
                    break;
                case 4:
                    system.displayTeamCalendar();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1, 2, 3, 4, or 5.");
            }
        }
    }

    private static void submitLeaveFlow(Employee emp) {
        System.out.println("\nSelect Leave Type:");
        System.out.println("1. CASUAL");
        System.out.println("2. SICK");
        System.out.println("3. ANNUAL");
        System.out.println("4. EMERGENCY");

        int typeChoice = readIntInput("Enter choice: ");
        LeaveType type;
        switch (typeChoice) {
            case 1: type = LeaveType.CASUAL; break;
            case 2: type = LeaveType.SICK; break;
            case 3: type = LeaveType.ANNUAL; break;
            case 4: type = LeaveType.EMERGENCY; break;
            default:
                System.out.println("Error: Invalid leave type choice!");
                return;
        }

        LocalDate startDate = readDateInput("\nEnter Start Date (YYYY-MM-DD): ");
        if (startDate == null) return;

        LocalDate endDate = readDateInput("Enter End Date (YYYY-MM-DD): ");
        if (endDate == null) return;

        if (endDate.isBefore(startDate)) {
            System.out.println("\nError: End date cannot be before start date!");
            return;
        }

        System.out.print("\nEnter Reason: ");
        String reason = scanner.nextLine();

        if (reason.trim().isEmpty()) {
            System.out.println("Error: Leave reason cannot be empty!");
            return;
        }

        LeaveRequest request = system.submitLeaveRequest(emp.getEmployeeId(), type, startDate, endDate, reason);
        if (request != null) {
            System.out.println("\nLeave request submitted successfully!");
            System.out.println("Request ID: " + request.getRequestId());
            System.out.println("Duration  : " + request.getDuration() + " days");
            System.out.println("Status    : " + request.getStatus());
        }
    }

    private static void viewEmployeeRequestsFlow(Employee emp) {
        List<LeaveRequest> requests = system.getEmployeeRequests(emp.getEmployeeId());
        if (requests.isEmpty()) {
            System.out.println("\nYou have no leave requests.");
            return;
        }

        System.out.println("\n========== MY LEAVE REQUESTS ==========");
        for (LeaveRequest req : requests) {
            req.displayRequestDetails();
        }
    }

    private static void viewPendingRequestsFlow() {
        List<LeaveRequest> pending = system.getPendingRequests();
        if (pending.isEmpty()) {
            System.out.println("\nNo pending leave requests found.");
            return;
        }

        System.out.println("\n========== PENDING REQUESTS ==========");
        for (LeaveRequest req : pending) {
            req.displayRequestDetails();
        }
    }

    private static void approveRequestFlow(Manager manager) {
        viewPendingRequestsFlow();
        List<LeaveRequest> pending = system.getPendingRequests();
        if (pending.isEmpty()) return;

        int reqId = readIntInput("\nEnter Request ID to Approve: ");
        system.approveRequest(reqId, manager);
    }

    private static void rejectRequestFlow(Manager manager) {
        viewPendingRequestsFlow();
        List<LeaveRequest> pending = system.getPendingRequests();
        if (pending.isEmpty()) return;

        int reqId = readIntInput("\nEnter Request ID to Reject: ");
        system.rejectRequest(reqId, manager);
    }

    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    private static LocalDate readDateInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        try {
            return LocalDate.parse(input.trim());
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format! Please use YYYY-MM-DD format (e.g. 2026-09-01).");
            return null;
        }
    }
}
