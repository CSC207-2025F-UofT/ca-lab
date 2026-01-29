package use_case.watchlist;

import org.json.JSONObject;

public class WatchlistInputData {

    private final String username;
    private final JSONObject watchlistInputData;

    public WatchlistInputData(String username, JSONObject watchlistInputData) {
        this.username = username;
        this.watchlistInputData = watchlistInputData;
    }
    public JSONObject getWatchlistInputData() {
        return watchlistInputData;
    }
    public String getUsername() {
        return username; }
}
