package use_case.loyalty_score;

import use_case.logout.LogoutOutputData;

public interface LoyaltyScoreOutputBoundary {
    /**
     * Prepares the view for the LoyaltyScore Use Case.
     * @param outputData the output data
     */
    void prepareView(LoyaltyScoreOutputData outputData);


    /**
     * Return to the previous the view.
     */
    void returnPreviousView();
}
