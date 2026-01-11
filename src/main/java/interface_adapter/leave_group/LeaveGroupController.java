package interface_adapter.leave_group;

import entity.Group;
import entity.SpotifyUser;
import use_case.leave_group.LeaveGroupInputBoundary;
import use_case.leave_group.LeaveGroupInputData;

/**
 * The controller for the Leave Group Use Case.
 */

public class LeaveGroupController {

    private final LeaveGroupInputBoundary leaveGroupUseCaseInteractor;

    public LeaveGroupController(LeaveGroupInputBoundary leaveGroupUseCaseInteractor) {
        this.leaveGroupUseCaseInteractor = leaveGroupUseCaseInteractor;
    }

    /**
     * Executes the Leave Group Use Case.
     */
    public void execute(SpotifyUser user, Group group) {
        LeaveGroupInputData inputData = new LeaveGroupInputData(user, group);
        leaveGroupUseCaseInteractor.execute(inputData);
    }
}