package use_case.manage_application;

import entity.Application;

import java.util.List;

public class ManageApplicationInteractor implements ManageApplicationInputBoundary {
    private final ManageApplicationDataAccessInterface dataAccessInterface;
    private final ManageApplicationOutputBoundary presenter;


    public ManageApplicationInteractor(ManageApplicationDataAccessInterface dataAccessInterface,
                                       ManageApplicationOutputBoundary presenter) {
        this.dataAccessInterface = dataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        // get all applications from DAO using DataAccessInterface methods
        List<Application> applications = dataAccessInterface.getAllApplications();

        // now we wrap the data into outputData container
        ManageApplicationOutputData outputData = new ManageApplicationOutputData(applications);

        // Send this wrapped data to the presenter for it to present!
        presenter.present(outputData);
    }




}

