package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private LoggedInViewModel loggedInViewModel;
    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;

    public LogoutPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                           LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData response) {
        // ✅ Update LoggedInState
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(""); // clear username after logout
        loggedInViewModel.firePropertyChanged();

        // ✅ Update LoginState
        LoginState loginState = loginViewModel.getState();
        loginState.setUsername(response.getUsername()); // prefill username of logged-out user
        loginState.setPassword(""); // clear password field
        loginViewModel.setState(loginState);
        loginViewModel.firePropertyChanged();

        // ✅ Switch back to LoginView
        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
