package interface_adapter.leaderboard;

import use_case.leaderboard.*;

/**
 * Leaderboard presenter.
 */
public class LeaderboardPresenter implements LeaderboardOutputBoundary {

    private final LeaderboardViewModel leaderboardViewModel;

    public LeaderboardPresenter(LeaderboardViewModel leaderboardViewModel) {
        this.leaderboardViewModel = leaderboardViewModel;
    }

    @Override
    public void changePagePrepareSuccessView(ChangePageOutputData results) {

        LeaderboardState state = leaderboardViewModel.getState();

        state.setUserRankPairs(results.getUserRankPairs());
        state.setPage(results.getNewPage());

        leaderboardViewModel.firePropertyChange();
    }

    @Override
    public void changePagePrepareFailedView(String error) {
        System.out.println(error);
    }

}
