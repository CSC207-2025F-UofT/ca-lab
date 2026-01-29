package interface_adapter.create_group;

import use_case.create_group.CreateGroupOutputBoundary;
import use_case.create_group.CreateGroupOutputData;

/**
 * Presenter for the Create Group use case.
 * Converts output data into a form suitable for the UI and updates the ViewModel.
 */
public class CreateGroupPresenter implements CreateGroupOutputBoundary {

    private final CreateGroupViewModel viewModel;

    /**
     * Constructs a CreateGroupPresenter.
     *
     * @param viewModel the ViewModel associated with the Create Group screen
     */
    public CreateGroupPresenter(CreateGroupViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Updates the ViewModel based on output data from the interactor.
     *
     * @param outputData the output data containing the created group info
     */
    @Override
    public void present(CreateGroupOutputData outputData) {
        if (outputData != null) {
            // Correct method names
            viewModel.setGroupName(outputData.getGroup_name());
            viewModel.setSuccess(true);
        } else {
            viewModel.setSuccess(false);
            viewModel.setErrorMessage("Failed to create group.");
        }
    }
}
