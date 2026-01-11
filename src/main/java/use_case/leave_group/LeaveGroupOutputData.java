package use_case.leave_group;

/**
 * Output Data for the Leave Group Use Case.
 */

public class LeaveGroupOutputData {

    private final String groupName;

    public LeaveGroupOutputData(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
