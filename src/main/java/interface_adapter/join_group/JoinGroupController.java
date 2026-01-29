package interface_adapter.join_group;

import entity.SpotifyUser;
import use_case.joingroup.JoinGroupInputBoundary;
import use_case.joingroup.JoinGroupInputData;

public class JoinGroupController {

    private final JoinGroupInputBoundary joinGroupInteractor;

    public JoinGroupController(JoinGroupInputBoundary joinGroupInteractor) {
        this.joinGroupInteractor = joinGroupInteractor;
    }

    public void joinGroup(String groupCode, SpotifyUser user) {
        JoinGroupInputData inputData = new JoinGroupInputData(groupCode, user);
        joinGroupInteractor.joinGroup(inputData);
    }
}
