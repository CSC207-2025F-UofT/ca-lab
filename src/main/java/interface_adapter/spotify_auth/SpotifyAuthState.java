package interface_adapter.spotify_auth;

public class SpotifyAuthState {
    private String authorizationUrl = "";
    private String authorizationCode = "";
    private String authError = "";
    private String username = "";
    private boolean isAuthenticated = false;
    private boolean isWaitingForCallback = false;  // NEW
    private String statusMessage = "";  // NEW

    public SpotifyAuthState() {}

    public SpotifyAuthState(SpotifyAuthState copy) {
        authorizationUrl = copy.authorizationUrl;
        authorizationCode = copy.authorizationCode;
        authError = copy.authError;
        username = copy.username;
        isAuthenticated = copy.isAuthenticated;
        isWaitingForCallback = copy.isWaitingForCallback;
        statusMessage = copy.statusMessage;
    }

    // Existing getters and setters
    public String getAuthorizationUrl() { return authorizationUrl; }
    public void setAuthorizationUrl(String authorizationUrl) { this.authorizationUrl = authorizationUrl; }

    public String getAuthorizationCode() { return authorizationCode; }
    public void setAuthorizationCode(String authorizationCode) { this.authorizationCode = authorizationCode; }

    public String getAuthError() { return authError; }
    public void setAuthError(String authError) { this.authError = authError; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isAuthenticated() { return isAuthenticated; }
    public void setAuthenticated(boolean authenticated) { isAuthenticated = authenticated; }

    // NEW getters and setters
    public boolean isWaitingForCallback() { return isWaitingForCallback; }
    public void setWaitingForCallback(boolean waitingForCallback) {
        isWaitingForCallback = waitingForCallback;
    }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }
}
