package use_case.logout;

/**
 * The Logout Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private LogoutUserDataAccessInterface userDataAccessObject;
    private LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessInterface,
                            LogoutOutputBoundary logoutOutputBoundary) {
        // TODO: save the DAO and Presenter in the instance variables.
    }

    @Override
    public void execute() {
        String username = userDataAccessObject.getCurrentUsername();
        userDataAccessObject.setCurrentUsername(null);
        LogoutOutputData outputData = new LogoutOutputData(username);
        logoutPresenter.prepareSuccessView(outputData);
        // * set the current username to null in the DAO
        // * instantiate the `LogoutOutputData`, which needs to contain the username.
        // * tell the presenter to prepare a success view.
    }
}

