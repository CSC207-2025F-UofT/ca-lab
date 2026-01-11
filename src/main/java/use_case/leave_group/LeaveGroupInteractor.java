package use_case.leave_group;

import entity.SpotifyUser;

/**
 * Use Case Interactor for the Leave Group Use Case.
 */
public class LeaveGroupInteractor implements LeaveGroupInputBoundary {

    private final LeaveGroupDataAccessInterface leaveGroupDAO;
    private final LeaveGroupOutputBoundary groupPresenter;

    public LeaveGroupInteractor(LeaveGroupDataAccessInterface leaveGroupDAO, LeaveGroupOutputBoundary presenter) {
        this.leaveGroupDAO = leaveGroupDAO;
        this.groupPresenter = presenter;
    }

    /**
     * Takes info from the user and group to remove the user from the group
     * @param inputData the input data for leaving a group
     */
    @Override
    public void execute(LeaveGroupInputData inputData) {
        final SpotifyUser user = inputData.getUser();
        final String groupName = inputData.getGroupName();

        //TODO: go into storage for group to users and remove use from group
        leaveGroupDAO.removeUserFromGroup(user, groupName);
        inputData.getGroup().removeUser(user);
        final LeaveGroupOutputData outputData = new LeaveGroupOutputData(groupName);
        groupPresenter.present(outputData);
    }
}
