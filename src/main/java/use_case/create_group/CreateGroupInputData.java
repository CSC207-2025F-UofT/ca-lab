package use_case.create_group;

import entity.SpotifyUser;

import java.util.List;

/**
 * The Input Data for the Create Group Use Case.
 */
public class CreateGroupInputData {

    private final List<SpotifyUser> users;
    private final String group_name;


    public CreateGroupInputData(String group_name, SpotifyUser spotifyUser, List<SpotifyUser> users) {
        if (group_name == null || group_name.isBlank()) {
            throw new IllegalArgumentException("Group name is invalid.");
        }
        if (users != null && users.size() > 7) {
            throw new IllegalArgumentException("Initial members cannot exceed 6 (owner counts as 1).");
        }

        this.group_name = group_name;
        this.users = users;
    }

    public String getGroup_name() { return group_name; }


    public List<SpotifyUser> getUsers () { return users; }
}

