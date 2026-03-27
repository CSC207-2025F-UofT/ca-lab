package use_case.watchlist;

import org.json.JSONObject;

import java.util.ArrayList;

public class WatchlistOutputData {
    private final ArrayList<JSONObject> watchlist;
    public WatchlistOutputData(ArrayList<JSONObject> watchlist) {
        this.watchlist = watchlist;
    }
    public ArrayList<JSONObject> getWatchlist() {
        return watchlist;
    }
}
