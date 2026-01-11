package use_case.leave_group;

import entity.Group;
import entity.SpotifyUser;

/**
 * Input Data for the Leave Group Use Case.
 */
public class LeaveGroupInputData {

    private final SpotifyUser user;

    private final Group group;

    public LeaveGroupInputData(final SpotifyUser user, final Group group) {
        this.user = user;
        this.group = group;
    }

    public SpotifyUser getUser() { return user; }

    public Group getGroup() { return group; }

    public String getUserid() { return user.getSpotifyUserId(); }

    public String getGroupName() {return group.getGroup_name(); }
}
