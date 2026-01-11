package interface_adapter.logged_in;

public class LoggedInState {
    private String username = "";
    private String password = "";
    private String passwordError;
    private boolean spotifyAuthenticated = false;
    private String spotifyUserId = "";

    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;
        spotifyAuthenticated = copy.spotifyAuthenticated; // NEW
        spotifyUserId = copy.spotifyUserId; // NEW
    }

    public LoggedInState() {}

    // Existing getters and setters...
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return password; }
    public void setPasswordError(String passwordError) { this.passwordError = passwordError; }
    public String getPasswordError() { return passwordError; }

    // NEW getters and setters for Spotify
    public boolean isSpotifyAuthenticated() { return spotifyAuthenticated; }
    public void setSpotifyAuthenticated(boolean spotifyAuthenticated) { this.spotifyAuthenticated = spotifyAuthenticated; }
    public String getSpotifyUserId() { return spotifyUserId; }
    public void setSpotifyUserId(String spotifyUserId) { this.spotifyUserId = spotifyUserId; }
}