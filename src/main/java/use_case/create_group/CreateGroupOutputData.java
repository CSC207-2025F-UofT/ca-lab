package use_case.create_group;

import entity.SpotifyUser;

import java.util.List;

/**
 * Output Data for the Create Group Use Case.
 */
public class CreateGroupOutputData {

    private final String group_name;
    private boolean success;
    private final List<SpotifyUser> users;


    /**
     * Constructs CreateGroupOutputData object
     *
     * @param group_name name of newly created group
     */

    public CreateGroupOutputData(String group_name, List<SpotifyUser> users) {
        this.group_name = group_name;
        this.success = success;
        this.users = users;
}

    public String getGroup_name() {
        return group_name;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<SpotifyUser> getUsers() { return users; }
}

