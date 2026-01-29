package use_case.spotify_auth;

/**
 * Interface for opening URLs in browser - allows for testing and flexibility
 */
public interface BrowserLauncher {
    /**
     * Opens the given URL in the default browser
     * @param url the URL to open
     * @throws Exception if unable to open browser
     */
    void openUrl(String url) throws Exception;
}
