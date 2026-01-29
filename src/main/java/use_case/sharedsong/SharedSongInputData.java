package use_case.sharedsong;

import entity.Group;
import entity.SpotifyUser;
import java.util.List;

/**
 * The input data for the Check Shared Song Use Case.
 */
public class SharedSongInputData {

    private final Group group;
    private final SpotifyUser user;

    public SharedSongInputData(SpotifyUser user, Group group) {
        this.group = group;
        this.user = user;
    }
    public List<SpotifyUser> getListOfMembers() {return group.getUsers();}
    public SpotifyUser getUser() {return user;}
}

