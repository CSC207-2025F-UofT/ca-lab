package use_case.create_group;

import entity.Group;
import entity.SpotifyUser;

import java.util.List;

/**
 * Output Data for the Create Group Use Case.
 */
public class CreateGroupOutputData {

    private final String group_name;
    private boolean success;
    private final List<SpotifyUser> users;
    private final Group group;     // store the Group object

    /**
     * Constructs CreateGroupOutputData object
     *
     * @param group_name name of newly created group
     * @param users list of users in the group
     * @param group the Group entity
     */

    public CreateGroupOutputData(String group_name, List<SpotifyUser> users, Group group) {
        this.group_name = group_name;
        this.users = users;
        this.group = group;
}

    public String getGroup_name() {
        return group_name;
    }

    public Group getGroup() {
        return group;
    }

    public List<SpotifyUser> getUsers() {
        return users;
    }}

