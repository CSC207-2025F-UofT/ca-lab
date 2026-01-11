package use_case.leave_group;

import entity.SpotifyUser;

/**
 * The DAO interface for the leave group use case.
 */
public interface LeaveGroupDataAccessInterface {

    /**
     * Removes a user from a group
     * @param groupName group to remove user from
     * @param user user to remove from group
     */
    void removeUserFromGroup(SpotifyUser user, String groupName);
}
