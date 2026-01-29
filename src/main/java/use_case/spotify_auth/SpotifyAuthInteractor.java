package use_case.spotify_auth;

import data_access.SpotifyDataAccessObject;
import data_access.SpotifyUserDataAccessObject;
import entity.SpotifyUser;
import util.CallbackServer;

public class SpotifyAuthInteractor implements SpotifyAuthInputBoundary {
    private final SpotifyDataAccessObject spotifyDataAccessObject;
    private final SpotifyAuthOutputBoundary presenter;
    private final BrowserLauncher browserLauncher;
    private CallbackServer callbackServer;

    public SpotifyAuthInteractor(SpotifyDataAccessObject spotifyDataAccessObject,
                                 SpotifyAuthOutputBoundary presenter,
                                 BrowserLauncher browserLauncher) {
        this.spotifyDataAccessObject = spotifyDataAccessObject;
        this.presenter = presenter;
        this.browserLauncher = browserLauncher;
    }

    @Override
    public void startAuthorization(String username) {
        new Thread(() -> {
            try {
                // Get authorization URL
                String authUrl = spotifyDataAccessObject.getAuthorizationUrl();
                if (authUrl == null) {
                    presenter.prepareFailView("Failed to generate authorization URL");
                    return;
                }

                // Notify presenter to update UI and open browser
                presenter.prepareAuthUrlView(authUrl);

                // Start callback server
                callbackServer = new CallbackServer();
                presenter.updateStatus("Waiting for authorization...");

                // Wait for callback (blocking)
                String code = callbackServer.startAndWaitForCode(120);

                // Exchange code for tokens
                presenter.updateStatus("Completing authorization...");
                SpotifyUser spotifyUser = spotifyDataAccessObject.exchangeCodeForTokens(code, username);
                SpotifyUserDataAccessObject.getInstance().addUser(spotifyUser);
                System.out.println("All users in DAO after Spotify login:");
                SpotifyUserDataAccessObject.getInstance()
                        .getAllUsers()
                        .forEach(u -> System.out.println(u.getUsername()));

                SpotifyAuthOutputData outputData = new SpotifyAuthOutputData(
                        spotifyUser.getUsername(),
                        spotifyUser.getSpotifyUserId(),
                        true,
                        spotifyUser
                );

                presenter.prepareSuccessView(outputData);

            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                    presenter.prepareFailView("Authorization timed out. Please try again.");
                } else {
                    presenter.prepareFailView("Authorization failed: " + e.getMessage());
                }
            }
        }).start();
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

            // debug print to verify
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