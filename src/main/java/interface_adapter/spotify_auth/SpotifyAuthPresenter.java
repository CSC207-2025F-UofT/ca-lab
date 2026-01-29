package interface_adapter.spotify_auth;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.spotify_auth.SpotifyAuthOutputBoundary;
import use_case.spotify_auth.SpotifyAuthOutputData;
import view.LoggedInView;

public class SpotifyAuthPresenter implements SpotifyAuthOutputBoundary {
    private final SpotifyAuthViewModel spotifyAuthViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private LoggedInView loggedInView;

    public SpotifyAuthPresenter(ViewManagerModel viewManagerModel,
                                SpotifyAuthViewModel spotifyAuthViewModel,
                                LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.spotifyAuthViewModel = spotifyAuthViewModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    public void setLoggedInView(LoggedInView view) {
        this.loggedInView = view;
    }

    @Override
    public void prepareAuthUrlView(String authUrl) {
        SpotifyAuthState currentState = spotifyAuthViewModel.getState();
        currentState.setAuthorizationUrl(authUrl);
        currentState.setWaitingForCallback(true);
        currentState.setStatusMessage("Opening browser...");
        currentState.setAuthError("");

        spotifyAuthViewModel.setState(currentState);
        spotifyAuthViewModel.firePropertyChange("authUrlReady");
    }

    @Override
    public void updateStatus(String message) {
        SpotifyAuthState currentState = spotifyAuthViewModel.getState();
        currentState.setStatusMessage(message);

        spotifyAuthViewModel.setState(currentState);
        spotifyAuthViewModel.firePropertyChange("statusUpdate");
    }

    @Override
    public void prepareSuccessView(SpotifyAuthOutputData response) {
        // Update auth state
        SpotifyAuthState authState = spotifyAuthViewModel.getState();
        authState.setAuthenticated(true);
        authState.setWaitingForCallback(false);
        authState.setStatusMessage("Connected successfully!");
        authState.setAuthError("");
        spotifyAuthViewModel.setState(authState);
        spotifyAuthViewModel.firePropertyChange("authSuccess");

        // Update logged in state
        LoggedInState loggedInState = loggedInViewModel.getState();
        if (loggedInState.getUsername() == null || loggedInState.getUsername().isEmpty()) {
            loggedInState.setUsername(response.getUsername());
        }
        loggedInState.setSpotifyAuthenticated(true);
        loggedInState.setSpotifyUserId(response.getSpotifyUserId());

        loggedInViewModel.setState(loggedInState);
        loggedInViewModel.firePropertyChange();

        // Pass SpotifyUser to view
        if (loggedInView != null && response.getSpotifyUser() != null) {
            loggedInView.setCurrentSpotifyUser(response.getSpotifyUser());
        }

        // Switch to logged in view
        viewManagerModel.setState(loggedInViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        SpotifyAuthState currentState = spotifyAuthViewModel.getState();
        currentState.setAuthError(error);
        currentState.setWaitingForCallback(false);
        currentState.setStatusMessage("");

        spotifyAuthViewModel.setState(currentState);
        spotifyAuthViewModel.firePropertyChange("authError");
    }
}