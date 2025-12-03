package use_case.leaderboard;

/**
 * Leaderboard input boundary.
 */
public interface LeaderboardInputBoundary {

    /**
     * Execute the change page function:
     * Derive the new sublist of user-rank pairs to display.
     * @param changePageInputData input data.
     */
    void changePage(ChangePageInputData changePageInputData);

}
