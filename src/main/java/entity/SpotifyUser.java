package entity;

public class SpotifyUser {
    private final String username;
    private final String accessToken;
    private final String refreshToken;
    private final String spotifyUserId;

    public SpotifyUser(String username, String accessToken, String refreshToken, String spotifyUserId) {
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.spotifyUserId = spotifyUserId;
    }

    // Getters
    public String getUsername() { return username; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getSpotifyUserId() { return spotifyUserId; }
}