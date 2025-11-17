package use_case.create_group;

import entity.User;

import java.util.List;

/**
 * Output Data for the Create Group Use Case.
 */
public class CreateGroupOutputData {

    private final String group_name;
    private boolean success;
    private final User owner;
    private final List<User> users;


    /**
     * Constructs CreateGroupOutputData object
     *
     * @param group_name name of newly created group
     */

    public CreateGroupOutputData(String group_name, User owner, List<User> users) {
        this.group_name = group_name;
        this.success = success;
        this.owner = owner;
        this.users = users;
}

    public String getGroup_name() {
        return group_name;
    }

    public boolean isSuccess() {
        return success;
    }
    public User getOwner() { return owner; }

    public List<User> getUsers() { return users; }
}

