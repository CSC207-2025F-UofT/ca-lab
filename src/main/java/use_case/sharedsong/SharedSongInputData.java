package use_case.sharedsong;

import entity.Group;
import entity.SpotifyUser;
import java.util.List;

/**
 * The input data for the Check Shared Song Use Case.
 */
public class SharedSongInputData {

    private final List<SpotifyUser> groupUsers;
    private final SpotifyUser user;

    public SharedSongInputData(SpotifyUser user, List<SpotifyUser> groupUsers) {
        this.groupUsers = groupUsers;
        this.user = user;
    }
    public List<SpotifyUser> getListOfMembers() {return groupUsers;}

    public SpotifyUser getUser() {return user;}
}

