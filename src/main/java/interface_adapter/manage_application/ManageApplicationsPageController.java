package interface_adapter.manage_application;

import interface_adapter.ViewManagerModel;
import interface_adapter.submit_application.SubmitViewModel;

public class ManageApplicationsPageController {

    private final ViewManagerModel viewManagerModel;
    private final ManageApplicationViewModel manageApplicationViewModel;

    public ManageApplicationsPageController(ViewManagerModel viewManagerModel,
                                            ManageApplicationViewModel manageApplicationViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.manageApplicationViewModel = manageApplicationViewModel;
    }

    public void execute() {
        viewManagerModel.setState(manageApplicationViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
