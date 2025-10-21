package interface_adapter.logout;

import use_case.login.LoginInputData;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputData;

/**
 * The controller for the Logout Use Case.
 */
public class LogoutController {

    private final LogoutInputBoundary logoutUseCaseInteractor;

    public LogoutController(LogoutInputBoundary logoutUseCaseInteractor) {
        // TODO: Save the interactor in the instance variable.
        this.logoutUseCaseInteractor = logoutUseCaseInteractor;

    }

    /**
     * Executes the Logout Use Case.
     */
    public void execute() {
        // TODO: run the use case interactor for the logout use case
        logoutUseCaseInteractor.execute();
    }
}
