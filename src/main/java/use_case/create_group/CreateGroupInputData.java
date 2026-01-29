package use_case.create_group;

import entity.SpotifyUser;
import java.util.List;

/**
 * The Input Data for the Create Group Use Case.
 */
public class CreateGroupInputData {

    private final String group_name;
    private final List<SpotifyUser> initialMembers;

    public CreateGroupInputData(String group_name, List<SpotifyUser> initialMembers) {
        if (group_name == null || group_name.isBlank()) {
            throw new IllegalArgumentException("Group name is invalid.");
        }
        if (initialMembers != null && initialMembers.size() > 7) {
            throw new IllegalArgumentException("Initial members cannot exceed 7.");
        }

        this.group_name = group_name;
        this.initialMembers = initialMembers;
    }

    public String getGroup_name() {
        return group_name;
    }

    public List<SpotifyUser> getInitialMembers() { //the users you add upon creation of the group, after the group is created you can add users and
        return initialMembers;
    }
}
