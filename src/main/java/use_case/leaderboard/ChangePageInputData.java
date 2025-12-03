package use_case.leaderboard;

/**
 * Change page function input data.
 * Attributes:
 * - newPage: page number (pragmatic) to change to.
 */
public class ChangePageInputData {

    private final int newPage;

    public ChangePageInputData(int newPage) { this.newPage = newPage; }

    int getNewPage() { return newPage; }

}
