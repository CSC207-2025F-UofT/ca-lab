package interface_adapter.spotify_auth;

import use_case.spotify_auth.SpotifyAuthInputBoundary;
import use_case.spotify_auth.SpotifyAuthInputData;

public class SpotifyAuthController {
    private final SpotifyAuthInputBoundary interactor;

    public SpotifyAuthController(SpotifyAuthInputBoundary interactor) {
        this.interactor = interactor;
    }

    public String getAuthorizationUrl() {
        return interactor.getAuthorizationUrl();
    }

    public void execute(String authorizationCode, String username) {
        SpotifyAuthInputData inputData = new SpotifyAuthInputData(authorizationCode, username);
        interactor.execute(inputData);
    }
}
