package use_case.joingroup;

public class JoinGroupOutputData {
    private final String groupName;
    private final boolean success;
    private final String message;

    public JoinGroupOutputData(String groupName, boolean success, String message) {
        this.groupName = groupName;
        this.success = success;
        this.message = message;
    }

    public String getGroupName() { return groupName; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
