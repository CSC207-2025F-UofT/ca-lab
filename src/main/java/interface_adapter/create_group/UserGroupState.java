package interface_adapter.create_group;

import entity.Group;
import entity.SpotifyUser;
import entity.UserTasteProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * The state for the Group View Model.
 */
public class UserGroupState {

    // input data that need to be passed to use case controllers
    private SpotifyUser spotifyUser;
    private List<SpotifyUser> groupUsers;
    private Group group;

    private String groupName = "";
    private List<String> groupUsernames = new ArrayList<>();
    private String nameError = "";

    public SpotifyUser getSpotifyUser() { return spotifyUser; }

    public void setSpotifyUser(SpotifyUser spotifyUser) { this.spotifyUser = spotifyUser; }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<SpotifyUser> getGroupUsers() { return this.groupUsers; }

    public void setGroupUsers(List<SpotifyUser> groupUsers) {
        this.groupUsers = new ArrayList<>(groupUsers);

        // fills user id list list
        this.groupUsernames = new ArrayList<>();
        for (SpotifyUser u : groupUsers) {
            this.groupUsernames.add(u.getUsername());
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getGroupUsernames() {
        return groupUsernames;
    }

    public void setGroupUsernames(List<String> groupUsernames) {
        this.groupUsernames = groupUsernames;
    }

    public String getNameError() { return nameError; }

    public void setNameError(String nameError) { this.nameError = nameError; }


}
