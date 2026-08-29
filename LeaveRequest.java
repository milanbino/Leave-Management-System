import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * LeaveRequest Class
 * Represents a leave application submitted by an employee.
 * Features AUTOMATIC DURATION CALCULATION using Java LocalDate & ChronoUnit.
 */
public class LeaveRequest {
    private int requestId;
    private Employee employee;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private long duration; // Calculated automatically in days
    private String reason;
    private LeaveStatus status;

    public LeaveRequest(int requestId, Employee employee, LeaveType leaveType, LocalDate startDate, LocalDate endDate, String reason) {
        this.requestId = requestId;
        this.employee = employee;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = LeaveStatus.PENDING; // Default status when created
        this.duration = calculateDuration(startDate, endDate); // Automatic calculation
    }

    /**
     * Private helper method to automatically calculate leave duration.
     * Uses ChronoUnit.DAYS.between() + 1 to include both start and end date.
     * Example: 2026-09-01 to 2026-09-03 yields 3 days.
     */
    private long calculateDuration(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

    // Getters and Setters (Encapsulation)
    public int getRequestId() {
        return requestId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public long getDuration() {
        return duration;
    }

    public String getReason() {
        return reason;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    // Display formatted request details for review
    public void displayRequestDetails() {
        System.out.println("----------------------------------------");
        System.out.println("Request ID : " + requestId);
        System.out.println("Employee   : " + employee.getName() + " (ID: " + employee.getEmployeeId() + ")");
        System.out.println("Leave Type : " + leaveType);
        System.out.println("Start Date : " + startDate);
        System.out.println("End Date   : " + endDate);
        System.out.println("Duration   : " + duration + " day(s)");
        System.out.println("Reason     : " + reason);
        System.out.println("Status     : " + status);
        System.out.println("----------------------------------------");
    }
}
