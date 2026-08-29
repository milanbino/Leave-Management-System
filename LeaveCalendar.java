import java.util.List;

/**
 * LeaveCalendar Class
 * Responsible for rendering and displaying the team-wide leave calendar.
 * Filters and shows ONLY APPROVED leave requests.
 */
public class LeaveCalendar {

    /**
     * Display team-wide leave calendar for approved requests.
     */
    public void displayTeamCalendar(List<LeaveRequest> requests) {
        System.out.println("\n===================================================================");
        System.out.println("                      TEAM LEAVE CALENDAR                          ");
        System.out.println("===================================================================");
        System.out.printf("%-15s %-15s %-12s %-12s %-10s\n", "Employee", "Leave Type", "Start Date", "End Date", "Duration");
        System.out.println("-------------------------------------------------------------------");

        boolean foundApproved = false;

        for (LeaveRequest req : requests) {
            // ONLY APPROVED requests appear on the team calendar
            if (req.getStatus() == LeaveStatus.APPROVED) {
                System.out.printf("%-15s %-15s %-12s %-12s %d day(s)\n",
                        req.getEmployee().getName(),
                        req.getLeaveType(),
                        req.getStartDate(),
                        req.getEndDate(),
                        req.getDuration());
                foundApproved = true;
            }
        }

        if (!foundApproved) {
            System.out.println("No approved leave requests found in the system.");
        }
        System.out.println("-------------------------------------------------------------------\n");
    }
}
