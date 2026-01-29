package use_case.watchlist;

import org.json.JSONObject;

public class RemoveWatchlistInputData {

    private final String username;
    private final JSONObject itemToRemove;

    public RemoveWatchlistInputData(String username, JSONObject itemToRemove) {
        this.username = username;
        this.itemToRemove = itemToRemove;
    }

    public String getUsername() {
        return username;
    }
    public JSONObject getItem() {
        return itemToRemove;
    }
}
