package interface_adapter.group_analytics;

import data_access.SpotifyDataAccessObject;
import entity.Group;
import entity.SpotifyUser;
import entity.UserTasteProfile;
import use_case.group_analytics.GroupAnalyticsInputBoundary;
import use_case.group_analytics.GroupAnalyticsInputData;

import java.util.List;

public class GroupAnalyticsController {

    private final GroupAnalyticsInputBoundary interactor;
    private final SpotifyDataAccessObject spotifyDAO;

    public GroupAnalyticsController(GroupAnalyticsInputBoundary interactor,
                                    SpotifyDataAccessObject spotifyDAO) {
        this.interactor = interactor;
        this.spotifyDAO = spotifyDAO;
    }

    // 1) Main real flow if you have a Group entity
    public void analyzeGroup(Group group) {
        List<UserTasteProfile> profiles =
                spotifyDAO.buildTasteProfilesForGroup(group);
        GroupAnalyticsInputData inputData = new GroupAnalyticsInputData(profiles);
        interactor.execute(inputData);
    }

    // 2) Real flow if you only have a list of SpotifyUser (e.g. from CreateGroupOutputData)
    public void analyzeFromUsers(List<SpotifyUser> users) {
        List<UserTasteProfile> profiles =
                spotifyDAO.buildTasteProfilesForUsers(users);
        GroupAnalyticsInputData inputData = new GroupAnalyticsInputData(profiles);
        interactor.execute(inputData);
    }

    // 3) Demo/testing flow: no Spotify call, you pass fake profiles
    public void analyzeProfiles(List<UserTasteProfile> profiles) {
        GroupAnalyticsInputData inputData = new GroupAnalyticsInputData(profiles);
        interactor.execute(inputData);
    }
}