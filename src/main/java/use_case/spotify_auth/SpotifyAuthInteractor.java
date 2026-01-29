package use_case.spotify_auth;

import data_access.SpotifyDataAccessObject;
import entity.SpotifyUser;

public class SpotifyAuthInteractor implements SpotifyAuthInputBoundary {
    private final SpotifyDataAccessObject spotifyDataAccessObject;
    private final SpotifyAuthOutputBoundary presenter;

    public SpotifyAuthInteractor(SpotifyDataAccessObject spotifyDataAccessObject,
                                 SpotifyAuthOutputBoundary presenter) {
        this.spotifyDataAccessObject = spotifyDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public String getAuthorizationUrl() {
        try {
            return spotifyDataAccessObject.getAuthorizationUrl();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void execute(SpotifyAuthInputData inputData) {
        try {
            SpotifyUser spotifyUser = spotifyDataAccessObject.exchangeCodeForTokens(
                    inputData.getAuthorizationCode(),
                    inputData.getUsername()
            );

            SpotifyAuthOutputData outputData = new SpotifyAuthOutputData(
                    spotifyUser.getUsername(),
                    spotifyUser.getSpotifyUserId(),
                    true,
                    spotifyUser  // NEW: Pass the full SpotifyUser object
            );

            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Authentication failed: " + e.getMessage());
        }
    }
}