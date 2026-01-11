package use_case.spotify_auth;

public interface SpotifyAuthInputBoundary {
    void execute(SpotifyAuthInputData inputData);
    String getAuthorizationUrl();
}
