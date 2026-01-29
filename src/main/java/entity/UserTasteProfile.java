package entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Lightweight representation of a user's taste for group analytics.
 * Built from a SpotifyUser's top genres.
 */
public class UserTasteProfile {

    private final String username;        // your app username
    private final String spotifyUserId;   // Spotify id, optional but nice to have
    private final Set<String> genres;

    public UserTasteProfile(String username, String spotifyUserId, Set<String> genres) {
        this.username = Objects.requireNonNull(username);
        this.spotifyUserId = spotifyUserId;
        Set<String> normalized = new HashSet<>();
        if (genres != null) {
            for (String g : genres) {
                if (g != null) {
                    normalized.add(g.trim().toLowerCase());
                }
            }
        }
        this.genres = normalized;
    }

    public String getUsername() {
        return username;
    }

    public String getSpotifyUserId() {
        return spotifyUserId;
    }

    public Set<String> getGenres() {
        return Collections.unmodifiableSet(genres);
    }
}
