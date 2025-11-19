package interface_adapter.manage_application;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import use_case.manage_application.ManageApplicationOutputBoundary;

public class ManageApplicationApplicationPresenter implements ManageApplicationOutputBoundary {


    private final ManageApplicationViewModel manageApplicationViewModel;
    private final ViewManagerModel viewManagerModel;

    public ManageApplicationApplicationPresenter() {

    }

    @Override
    public void prepareSuccessView(ManageApplicationViewModel manageApplicationViewModel) {
        


}