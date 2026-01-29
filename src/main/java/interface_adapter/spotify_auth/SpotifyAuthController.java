package interface_adapter.spotify_auth;

import use_case.spotify_auth.SpotifyAuthInputBoundary;
import use_case.spotify_auth.SpotifyAuthInputData;

public class SpotifyAuthController {
    private final SpotifyAuthInputBoundary interactor;

    public SpotifyAuthController(SpotifyAuthInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Initiates the authorization process
     * @param username the username to associate with the Spotify account
     */
    public void startAuthorization(String username) {
        interactor.startAuthorization(username);
    }

    /**
     * Completes authorization with the received code
     * @param authorizationCode the code from Spotify
     * @param username the username
     */
    public void completeAuthorization(String authorizationCode, String username) {
        SpotifyAuthInputData inputData = new SpotifyAuthInputData(authorizationCode, username);
        interactor.execute(inputData);
    }
}