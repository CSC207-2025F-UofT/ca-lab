package interface_adapter.create_group;

import interface_adapter.ViewManagerModel;
import use_case.create_group.CreateGroupOutputBoundary;
import use_case.create_group.CreateGroupOutputData;
import entity.SpotifyUser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Presenter for the Create Group use case.
 * Converts output data into a form suitable for the UI and updates the ViewModel.
 */
public class CreateGroupPresenter implements CreateGroupOutputBoundary {

    private final InGroupViewModel inGroupViewModel;
    private final NoGroupViewModel noGroupViewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructs a CreateGroupPresenter.
     *
     * @param noGroupViewModel the NoGroupViewModel associated with the Create Group screen.
     * @param inGroupViewModel the InGroupViewModel to be updated
     * @param viewManagerModel changes create/join group view to in group view
     */
    public CreateGroupPresenter(InGroupViewModel inGroupViewModel,
                                NoGroupViewModel noGroupViewModel,
                                ViewManagerModel viewManagerModel) {
        this.inGroupViewModel = inGroupViewModel;
        this.noGroupViewModel = noGroupViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Updates the ViewModel based on output data from the interactor.
     *
     * @param outputData the output data containing the created group info
     */
    @Override
    public void present(CreateGroupOutputData outputData) {
        if (outputData == null) {
            // Handle error case
            UserGroupState errorState = noGroupViewModel.getState();
            errorState.setNameError("Failed to create group");
            noGroupViewModel.firePropertyChange("createError");
            return;
        }

        // Update InGroupViewModel state
        UserGroupState currentState = new UserGroupState();
        currentState.setGroup(outputData.getGroup());               // full Group entity
        currentState.setGroupName(outputData.getGroup_name());

        currentState.setGroupUsers(outputData.getUsers());

        // Map SpotifyUser list to usernames for UI display
        List<String> usernames = outputData.getUsers()
                .stream()
                .map(SpotifyUser::getUsername)
                .collect(Collectors.toList());
        currentState.setGroupUsernames(usernames);

        // Set the creator as the SpotifyUser for state
        if (!outputData.getUsers().isEmpty()) {
            currentState.setSpotifyUser(outputData.getUsers().get(0));
        }

        // Notify the InGroupView to update UI
        inGroupViewModel.setState(currentState);
        inGroupViewModel.firePropertyChange();

        // Reset NoGroupViewModel state
        noGroupViewModel.setState(new UserGroupState());

        // Switch the view to InGroupView
        viewManagerModel.setState(inGroupViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}