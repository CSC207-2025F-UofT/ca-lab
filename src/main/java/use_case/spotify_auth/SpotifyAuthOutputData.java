package use_case.spotify_auth;

import entity.SpotifyUser;

public class SpotifyAuthOutputData {
    private final String username;
    private final String spotifyUserId;
    private final boolean success;
    private final SpotifyUser spotifyUser;  // NEW

    public SpotifyAuthOutputData(String username, String spotifyUserId, boolean success, SpotifyUser spotifyUser) {
        this.username = username;
        this.spotifyUserId = spotifyUserId;
        this.success = success;
        this.spotifyUser = spotifyUser;  // NEW
    }

    public String getUsername() {
        return username;
    }

    public String getSpotifyUserId() {
        return spotifyUserId;
    }

    public boolean isSuccess() {
        return success;
    }

    public SpotifyUser getSpotifyUser() {  // NEW
        return spotifyUser;
    }
}
