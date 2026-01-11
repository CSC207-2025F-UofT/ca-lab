package interface_adapter.create_group;


/**
 * ViewModel for the Create Group view.
 * Stores data to be observed by the UI layer.
 */

public class CreateGroupViewModel {

    private String group_name;
    private boolean success;
    private String errorMessage;

    public String getGroup_name() {
        return group_name;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setGroupName(String group_name) { this.group_name = group_name; }



    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}