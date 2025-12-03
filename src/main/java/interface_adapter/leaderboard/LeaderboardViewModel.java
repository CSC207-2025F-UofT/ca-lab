package interface_adapter.leaderboard;

import interface_adapter.ViewModel;

/**
 * Leaderboard view model.
 */
public class LeaderboardViewModel extends ViewModel<LeaderboardState> {

    public LeaderboardViewModel() {
        super("leaderboard");
        setState(new LeaderboardState());
    }
}
