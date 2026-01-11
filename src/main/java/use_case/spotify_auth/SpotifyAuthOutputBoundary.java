package use_case.spotify_auth;

public interface SpotifyAuthOutputBoundary {
    void prepareSuccessView(SpotifyAuthOutputData response);
    void prepareFailView(String error);
}