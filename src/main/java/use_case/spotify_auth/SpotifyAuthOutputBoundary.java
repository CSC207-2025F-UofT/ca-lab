package use_case.spotify_auth;

public interface SpotifyAuthOutputBoundary {
    /**
     * Prepares the view for starting authorization
     * @param authUrl the URL to open in browser
     */
    void prepareAuthUrlView(String authUrl);

    /**
     * Updates status during authorization
     * @param message the status message
     */
    void updateStatus(String message);

    /**
     * Prepares success view after authentication
     * @param response the success data
     */
    void prepareSuccessView(SpotifyAuthOutputData response);

    /**
     * Prepares failure view
     * @param error the error message
     */
    void prepareFailView(String error);
}