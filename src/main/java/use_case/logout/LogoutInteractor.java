package use_case.logout;

/**
 * The Logout Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private LogoutUserDataAccessInterface userDataAccessObject;
    private LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessInterface,
                            LogoutOutputBoundary logoutOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.logoutPresenter = logoutOutputBoundary;
    }
    @Override
    public void execute() {
        // Get current username before logging out
        String currentUsername = userDataAccessObject.getCurrentUsername();

        // Clear current user in the DAO
        userDataAccessObject.setCurrentUsername(null);

        // Create output data with the username that just logged out
        LogoutOutputData outputData = new LogoutOutputData(currentUsername);

        // Notify presenter of success
        logoutPresenter.prepareSuccessView(outputData);
    }
}

