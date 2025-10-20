package use_case.logout;

/**
 * The Logout Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;
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
        String username = userDataAccessObject.getCurrentUsername();
        userDataAccessObject.setCurrentUsername(null);
        // * instantiate the `LogoutOutputData`, which needs to contain the username.
        LogoutOutputData logoutOutputData = new LogoutOutputData(username);
        // * tell the presenter to prepare a success view.
        logoutPresenter.prepareSuccessView(logoutOutputData);
    }
}

