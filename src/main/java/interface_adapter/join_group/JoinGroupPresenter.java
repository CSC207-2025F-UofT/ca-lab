package interface_adapter.join_group;

import use_case.joingroup.JoinGroupOutputBoundary;
import use_case.joingroup.JoinGroupOutputData;

public class JoinGroupPresenter implements JoinGroupOutputBoundary {

    private final JoinGroupViewModel viewModel;

    public JoinGroupPresenter(JoinGroupViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(JoinGroupOutputData outputData) {
        viewModel.setSuccess(true);
        viewModel.setMessage(outputData.getMessage());
        viewModel.setGroupName(outputData.getGroupName());
        viewModel.firePropertyChanged(); // If using property change listeners
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setSuccess(false);
        viewModel.setMessage(errorMessage);
        viewModel.firePropertyChanged();
    }
}

