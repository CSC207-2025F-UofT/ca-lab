package interface_adapter.loyalty_score;

import entity.SpotifyUser;
import use_case.loyalty_score.LoyaltyScoreInputBoundary;
import use_case.loyalty_score.LoyaltyScoreInputData;

public class LoyaltyController {

    private final LoyaltyScoreInputBoundary loyaltyScoreInteractor;

    public LoyaltyController(LoyaltyScoreInputBoundary loyaltyScoreInteractor) {
        this.loyaltyScoreInteractor = loyaltyScoreInteractor;
    }

    /**
     * Executes the LoyaltyScore use case.
     */
    public void execute(SpotifyUser spotifyUser, String artistName) {
        LoyaltyScoreInputData inputData = new LoyaltyScoreInputData(spotifyUser, artistName);
        loyaltyScoreInteractor.execute(inputData);
    }
}
