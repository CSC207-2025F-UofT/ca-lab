package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;
import interface_adapter.login.LoginState;
import interface_adapter.logged_in.LoggedInState;


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
        // TODO: assign to the three instance variables.
    }

    @Override
    public void prepareSuccessView(LogoutOutputData response) {
        // We need to switch to the login view, which should have
        // an empty username and password.

        // We also need to set the username in the LoggedInState to
        // the empty string.

        // TODO: have prepareSuccessView update the LoggedInState
        // 1. get the LoggedInState out of the appropriate View Model,
        // 2. set the username in the state to the empty string
        // 3. firePropertyChanged so that the View that is listening is updated.

        // TODO: have prepareSuccessView update the LoginState
        // 1. get the LoginState out of the appropriate View Model,
        // 2. set the username in the state to be the username of the user that just logged out,
        // 3. firePropertyChanged so that the View that is listening is updated.

        final String username =
                (response != null && response.getUsername() != null && !response.getUsername().isEmpty())
                        ? response.getUsername()
                        : (loggedInViewModel.getState() != null ? loggedInViewModel.getState().getUsername() : "");

        LoginState loginState = loginViewModel.getState();
        if (loginState == null) loginState = new LoginState();

        loginState.setUsername(username);
        loginState.setPassword(null);
        loginState.setLoginError(null);

        loginViewModel.setState(loginState);
        loginViewModel.firePropertyChanged();

        LoggedInState liState = loggedInViewModel.getState();
        if (liState == null) liState = new LoggedInState();

        liState.setUsername(null);

        loggedInViewModel.setState(liState);
        loggedInViewModel.firePropertyChanged();

        // This code tells the View Manager to switch to the LoginView.
        this.viewManagerModel.setState(loginViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }
}
