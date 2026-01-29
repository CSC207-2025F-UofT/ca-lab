package use_case.spotify_auth;

public class SpotifyAuthInputData {
    private final String authorizationCode;
    private final String username;

    public SpotifyAuthInputData(String authorizationCode, String username) {
        this.authorizationCode = authorizationCode;
        this.username = username;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getUsername() {
        return username;
    }
}