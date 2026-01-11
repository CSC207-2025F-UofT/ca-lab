package use_case.spotify_auth;

import data_access.SpotifyDataAccessObject;
import data_access.SpotifyUserDataAccessObject;
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

            // Add the user to the shared DAO
            SpotifyUserDataAccessObject.getInstance().addUser(spotifyUser);

            // Optional: debug print to verify
            System.out.println("All users in DAO after Spotify login:");
            SpotifyUserDataAccessObject.getInstance().getAllUsers()
                    .forEach(u -> System.out.println(u.getUsername()));

            SpotifyAuthOutputData outputData = new SpotifyAuthOutputData(
                    spotifyUser.getUsername(),
                    spotifyUser.getSpotifyUserId(),
                    true,
                    spotifyUser
            );


            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Authentication failed: " + e.getMessage());
        }
    }
}