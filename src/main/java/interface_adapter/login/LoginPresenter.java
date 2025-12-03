package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import view.GameDashboard;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final GameDashboard dashboard; // renamed to avoid shadowing confusion

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel,
                          GameDashboard dashboard) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.dashboard = dashboard; // now valid — assigning the parameter to the field
    }

    /**
     * Prepares output data from the Login use case if no errors occur.
     *
     * @param response the output data
     */
    @Override
    public void prepareSuccessView(LoginOutputData response) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        loggedInViewModel.firePropertyChange();

        loginViewModel.setState(new LoginState());

        // Update dashboard UI with username
        dashboard.setUser(response.getUsername());

        viewManagerModel.setState("dashboard");
        viewManagerModel.firePropertyChange();
    }

    /**
     * Prepares output data from the Login use case if an error occurs.
     *
     * @param error the error message
     */
    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChange();
    }
}
