package interface_adapter.manage_application;

import use_case.manage_application.ManageApplicationOutputBoundary;
import use_case.manage_application.ManageApplicationOutputData;

public class ManageApplicationPresenter implements ManageApplicationOutputBoundary {

    private final ManageApplicationViewModel viewModel;

    public ManageApplicationPresenter(ManageApplicationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ManageApplicationOutputData outputData) {
        viewModel.setApplications(outputData.getApplications());
    }
}
