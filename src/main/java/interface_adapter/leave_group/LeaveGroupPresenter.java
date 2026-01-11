package interface_adapter.leave_group;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.InGroupViewModel;
import interface_adapter.create_group.NoGroupViewModel;
import interface_adapter.create_group.UserGroupState;
import use_case.leave_group.LeaveGroupOutputBoundary;
import use_case.leave_group.LeaveGroupOutputData;

/**
 * Presenter for the Leave Group Use Case.
 */
public class LeaveGroupPresenter implements LeaveGroupOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final InGroupViewModel inGroupViewModel;
    private final NoGroupViewModel noGroupViewModel;

//
    public LeaveGroupPresenter(ViewManagerModel viewManagerModel, InGroupViewModel inGroupViewModel, NoGroupViewModel noGroupViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.inGroupViewModel = inGroupViewModel;
        this.noGroupViewModel = noGroupViewModel;
    }

    @Override
    public void present(LeaveGroupOutputData response) {
        // Update UserGroupState - set in group to false
        final UserGroupState userGroupState = inGroupViewModel.getState();
        userGroupState.setInGroup(false);
        userGroupState.setGroupName("");
        userGroupState.setGroupUsernames(null);
        noGroupViewModel.setState(userGroupState);
        noGroupViewModel.firePropertyChange();

        //Switch to No Group View
        this.viewManagerModel.setState(inGroupViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
//
    }

}