package use_case.loyalty_score;

import java.util.Map;

/**
 * Output Data for the LoyaltyScore
 */

public class LoyaltyScoreOutputData {
    // key of String is date.
    private final Map<String, Integer> loyalty_scores;


    public LoyaltyScoreOutputData(Map<String, Integer> loyalty_scores)
    {
        this.loyalty_scores = loyalty_scores;
    }

    public Map<String, Integer> getScores() { return loyalty_scores; }

}
