package use_case.loyalty_score;

public interface LoyaltyScoreOutputBoundary {
    /**
     * Prepares the view for the LoyaltyScore Use Case.
     * @param outputData the output data
     */
    void prepareView(LoyaltyScoreOutputData outputData);

}
