package interface_adapter.leaderboard;

import use_case.leaderboard.*;

/**
 * Leaderboard controller.
 */
public class LeaderboardController {

    private final LeaderboardInputBoundary leaderboardInteractor;

    public LeaderboardController(LeaderboardInputBoundary leaderboardInteractor) {
        this.leaderboardInteractor = leaderboardInteractor;
    }

    /**
     * Execute the change page function.
     * @param newPage new page number to update to.
     */
    public void changePage(int newPage) {
        leaderboardInteractor.changePage(
                new ChangePageInputData(newPage)
        );
    }

}
