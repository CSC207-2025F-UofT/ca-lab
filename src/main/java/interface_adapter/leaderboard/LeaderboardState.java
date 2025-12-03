package interface_adapter.leaderboard;

import java.util.ArrayList;

/**
 * Leaderboard view model state.
 * Attributes:
 * - userRankPairs: current sublist of user-rank pairs being displayed.
 * - page: current page number.
 */
public class LeaderboardState {

    private ArrayList<Object[]> userRankPairs;
    private int page;

    public LeaderboardState() {
        userRankPairs = null;
        this.page = 1;
    }

    public ArrayList<Object[]> getUserRankPairs() { return userRankPairs; }
    public void setUserRankPairs(ArrayList<Object[]> userRankPairs) { this.userRankPairs = userRankPairs; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

}
