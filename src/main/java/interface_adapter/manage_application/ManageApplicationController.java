package interface_adapter.manage_application;

import use_case.manage_application.ManageApplicationInputBoundary;


public class ManageApplicationController {

    private final ManageApplicationInputBoundary interactor;

    public ManageApplicationController(ManageApplicationInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}
