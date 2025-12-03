package use_case.leaderboard;

import java.util.ArrayList;

/**
 * Change page function output data.
 * Attributes:
 * - userRankPairs: sublist of user-rank pairs to display.
 * - newPage: page number (pragmatic) to change to.
 */
public class ChangePageOutputData {

    private final ArrayList<Object[]> userRankPairs;
    private final int newPage;

    public ChangePageOutputData(ArrayList<Object[]> userRankPairs, int newPage) {
        this.userRankPairs = userRankPairs;
        this.newPage = newPage;
    }

    public ArrayList<Object[]> getUserRankPairs() { return userRankPairs; }

    public int getNewPage() { return newPage; }
}
