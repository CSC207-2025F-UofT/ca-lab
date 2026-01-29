package use_case.spotify_auth;

public interface SpotifyAuthInputBoundary {
    /**
     * Starts the authorization flow
     * @param username the username to associate with Spotify
     */
    void startAuthorization(String username);

    /**
     * Completes the authorization with the received code
     * @param inputData the authorization data
     */
    void execute(SpotifyAuthInputData inputData);
}