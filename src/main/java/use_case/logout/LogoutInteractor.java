package use_case.logout;

public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessInterface,
                            LogoutOutputBoundary logoutOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.logoutPresenter = logoutOutputBoundary;
    }

    @Override
    public void execute() {
        // Get the currently logged-in username
        String username = userDataAccessObject.getCurrentUsername();

        // Log out by clearing the current user in the DAO
        userDataAccessObject.setCurrentUsername(null);

        // Prepare output data to send to the presenter
        LogoutOutputData outputData = new LogoutOutputData(username);

        // Tell the presenter to update the view
        logoutPresenter.prepareSuccessView(outputData);

        System.out.println("LogoutInteractor: executed logout for " + username);
    }
}
