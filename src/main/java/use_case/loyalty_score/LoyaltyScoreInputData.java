package use_case.loyalty_score;

import entity.SpotifyUser;
import org.json.JSONArray;

/**
 * The Input Data for the Login Use Case.
 * artist_name is the artist of interest.
 */
public class LoyaltyScoreInputData {

    private final SpotifyUser spotifyUser;
    private final String artist_name;
    private JSONArray loyalty_scores;


    public LoyaltyScoreInputData(SpotifyUser currentSpotifyUser, String artistName) {
        this.spotifyUser = currentSpotifyUser;
        this.artist_name = artistName;
    }

    public SpotifyUser getSpotifyUser() {
        return spotifyUser;
    }

    public String getArtist_name() {
        return artist_name;
    }
}