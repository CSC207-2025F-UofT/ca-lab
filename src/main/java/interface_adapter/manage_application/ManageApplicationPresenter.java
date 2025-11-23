//package interface_adapter.manage_application;
//
//import interface_adapter.ViewManagerModel;
//import use_case.manage_application.ManageApplicationOutputBoundary;
//
//public class ManageApplicationPresenter implements ManageApplicationOutputBoundary {
//
//
//    private final ManageApplicationViewModel manageApplicationViewModel;
//    private final ViewManagerModel viewManagerModel;
//
//    public ManageApplicationPresenter() {
//
//    }
//
//    @Override
//    public void prepareSuccessView(ManageApplicationViewModel manageApplicationViewModel) {
//
//
//
//}
package interface_adapter.manage_application;

import use_case.manage_application.ManageApplicationOutputBoundary;
import use_case.manage_application.ManageApplicationOutputData;

import java.util.ArrayList;
import java.util.List;

public class ManageApplicationPresenter implements ManageApplicationOutputBoundary {

    private final ManageApplicationViewModel viewModel;

    public ManageApplicationPresenter(ManageApplicationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentApplications(ManageApplicationOutputData outputData) {

        List<ManageApplicationCardState> cardStates = new ArrayList<>();

        for (ManageApplicationOutputData.ApplicationData d : outputData.getApps()) {
            cardStates.add(new ManageApplicationCardState(d.id, d.title, d.status, d.subtitle));
        }

        ManageApplicationState state = new ManageApplicationState();
        viewModel.setState(state);
    }
}
