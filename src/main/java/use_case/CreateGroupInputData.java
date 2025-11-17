package use_case.create_group;

import entity.User;

import java.util.List;

/**
 * The Input Data for the Create Group Use Case.
 */
public class CreateGroupInputData {

    private final String group_name;
    private final User owner;
    private final List<User> initialMembers;


    public CreateGroupInputData(String group_name, User owner, List<User> initialMembers) {
        if (group_name == null || group_name.isBlank()) {
            throw new IllegalArgumentException("Group name is invalid.");
        }
        if (initialMembers != null && initialMembers.size() > 6) {
            throw new IllegalArgumentException("Initial members cannot exceed 6 (owner counts as 1).");
        }

        this.group_name = group_name;
        this.owner = owner;
        this.initialMembers = initialMembers;
    }

    public String getGroup_name() { return group_name; }

    public User getOwner() { return owner; }

    public List<User> getInitialMembers() { return initialMembers; }
}

