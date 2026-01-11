package use_case.loyalty_score;


/**
 * Input Boundary for calculating loyalty score.
 */
public interface LoyaltyScoreInputBoundary {

    /**
     * Executes the loyalty score use case
     * @param loyaltyScoreInputData the input data
     */
    void execute(LoyaltyScoreInputData loyaltyScoreInputData);

}