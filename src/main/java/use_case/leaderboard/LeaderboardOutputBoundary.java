package use_case.leaderboard;

/**
 * Leaderboard output boundary.
 */
public interface LeaderboardOutputBoundary {

    /**
     * Prepare the success view for the change page function:
     * Update the leaderboard view model state with new data and executeFirePropertyChange.
     * @param changePageOutputData output data.
     */
    void changePagePrepareSuccessView(ChangePageOutputData changePageOutputData);

    /**
     * Prepare the failed view for the change page function:
     * Print the error message; view does not change.
     * @param errorMessage error message.
     */
    void changePagePrepareFailedView(String errorMessage);

}
