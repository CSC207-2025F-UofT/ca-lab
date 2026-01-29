package interface_adapter.account;

import org.json.JSONObject;
import use_case.watchlist.*;

public class AccountController {

    private final WatchlistInputBoundary interactor;
    private final RemoveWatchlistInputBoundary removeInteractor;

    public AccountController(WatchlistInputBoundary watchlistInputBoundary,
                             RemoveWatchlistInputBoundary removeInteractor) {
        this.interactor = watchlistInputBoundary;
        this.removeInteractor = removeInteractor;
    }

    public void addToWatchlist(String username, JSONObject watchlistInputData) {
        WatchlistInputData inputData = new WatchlistInputData(username, watchlistInputData);
        interactor.execute(inputData);
    }

    public void removeFromWatchlist(String username, JSONObject item) {
        RemoveWatchlistInputData removeData =
                new RemoveWatchlistInputData(username, item);
        removeInteractor.execute(removeData);
    }

    public void loadWatchlist(String username) {
        WatchlistInputData inputData = new WatchlistInputData(username, null);
        interactor.execute(inputData);
    }
}
