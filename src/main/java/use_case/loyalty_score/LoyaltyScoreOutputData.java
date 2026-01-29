package use_case.loyalty_score;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Output Data for the LoyaltyScore
 */

public class LoyaltyScoreOutputData {
    // key of String is date.
    private final Map<String, Integer> loyalty_scores;

    // list of all previous dates calculated
    private final List<String> dates;

    private final String artist;


    public LoyaltyScoreOutputData(Map<String, Integer> loyalty_scores, List<String> dates, String artist_name)
    {
        this.loyalty_scores = loyalty_scores;
        this.dates = dates;
        this.artist = artist_name;
    }

    public Map<String, Integer> getScores() { return loyalty_scores; }
    public List<String> getDates() { return dates; }

    public String getArtistName() { return this.artist; }
}
