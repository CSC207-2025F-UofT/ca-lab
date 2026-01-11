package use_case.create_group;

import entity.Group;
import entity.SpotifyUser;

import java.util.List;

/**
 * Output Data for the Create Group Use Case.
 */
public class CreateGroupOutputData {

    private final String group_name;
    private final List<SpotifyUser> users;
    private final Group group;     // store the Group object
    private final String groupCode;

    public CreateGroupOutputData(String group_name, List<SpotifyUser> users, Group group, String groupCode) {
        this.group_name = group_name;
        this.users = users;
        this.group = group;
        this.groupCode = groupCode;
    }

    public String getGroup_name() {
        return group_name;
    }

    public Group getGroup() {
        return group;
    }

    public List<SpotifyUser> getUsers() {
        return users;
    }

    public String getGroupCode() {
        return groupCode;
    }
}
