package interface_adapter.account;

import org.json.JSONObject;
import use_case.watchlist.WatchlistInputBoundary;
import use_case.watchlist.WatchlistInputData;

public class AccountController {

    private final WatchlistInputBoundary interactor;

    public AccountController(WatchlistInputBoundary watchlistInputBoundary) {
        this.interactor = watchlistInputBoundary;
    }

    public void addToWatchlist(String username, JSONObject watchlistInputData) {
        WatchlistInputData inputData = new WatchlistInputData(username, watchlistInputData);
        interactor.execute(inputData);
    }

    public void loadWatchlist(String username) {
        WatchlistInputData inputData = new WatchlistInputData(username, null);
        interactor.execute(inputData);
    }
}
