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
        this.userDataAccessObject = userDataAccessInterface;
        this.logoutPresenter = logoutOutputBoundary;
    }

    @Override
    public void execute() {
        // TODO: implement the logic of the Logout Use Case
        // * set the current username to null in the DAO
        // * instantiate the `LogoutOutputData`, which needs to contain the username.
        // * tell the presenter to prepare a success view.
        // 1) capture who is currently logged in
        final String username = userDataAccessObject.getCurrentUsername();

        // 2) clear the session/current user
        userDataAccessObject.setCurrentUsername(null);

        // 3) send the (previous) username back to the presenter so it can prefill the LoginView
        final LogoutOutputData outputData = new LogoutOutputData(username);
        logoutPresenter.prepareSuccessView(outputData);
    }
}
