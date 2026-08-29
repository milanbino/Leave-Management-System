/**
 * Manager Class
 * Demonstrates INHERITANCE by extending Employee,
 * ABSTRACTION by implementing LeaveApprover, and
 * POLYMORPHISM by overriding the getRole() method.
 */
public class Manager extends Employee implements LeaveApprover {

    // Constructor calling the superclass (Employee) constructor
    public Manager(int employeeId, String name, String email, String department) {
        super(employeeId, name, email, department);
    }

    /**
     * Overriding getRole() method (Polymorphism)
     */
    @Override
    public String getRole() {
        return "Manager";
    }

    /**
     * Implementation of approveLeave from LeaveApprover interface (Abstraction)
     */
    @Override
    public boolean approveLeave(LeaveRequest request) {
        if (request == null) {
            System.out.println("Invalid leave request!");
            return false;
        }

        if (request.getStatus() != LeaveStatus.PENDING) {
            System.out.println("Error: Only PENDING requests can be approved.");
            return false;
        }

        request.setStatus(LeaveStatus.APPROVED);
        System.out.println("Leave request " + request.getRequestId() + " approved successfully.");
        return true;
    }

    /**
     * Implementation of rejectLeave from LeaveApprover interface (Abstraction)
     */
    @Override
    public boolean rejectLeave(LeaveRequest request) {
        if (request == null) {
            System.out.println("Invalid leave request!");
            return false;
        }

        if (request.getStatus() != LeaveStatus.PENDING) {
            System.out.println("Error: Only PENDING requests can be rejected.");
            return false;
        }

        request.setStatus(LeaveStatus.REJECTED);
        System.out.println("Leave request " + request.getRequestId() + " rejected.");
        return true;
    }
}
