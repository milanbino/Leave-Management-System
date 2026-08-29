/**
 * LeaveApprover Interface
 * Demonstrates ABSTRACTION by defining a contract for leave approval operations.
 * Any class implementing this interface must provide concrete implementations
 * for approving and rejecting leave requests.
 */
public interface LeaveApprover {
    boolean approveLeave(LeaveRequest request);
    boolean rejectLeave(LeaveRequest request);
}
