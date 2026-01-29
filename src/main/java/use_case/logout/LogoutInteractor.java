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
        // Get the current username before clearing it
        final String username = userDataAccessObject.getCurrentUsername();

        // Set current username to null in the DAO
        userDataAccessObject.setCurrentUsername(null);

        // Create output data with the username that was logged out
        final LogoutOutputData logoutOutputData = new LogoutOutputData(username);

        // Tell presenter to prepare success view
        logoutPresenter.prepareSuccessView(logoutOutputData);
    }
}
