package interface_adapter.account;

import org.json.JSONObject;

import java.util.ArrayList;

public class AccountState {
    private String username;
    private ArrayList<JSONObject> watchlist = new ArrayList<>();

    public ArrayList<JSONObject> getWatchlist() {
        if (watchlist == null) {
            watchlist = new ArrayList<>();
        }
        return watchlist;
    }

    public void setWatchlist(ArrayList<JSONObject> watchlist) {
        this.watchlist = watchlist;
    }

    public void addToWatchlist(JSONObject watchlistInputData) {
        this.watchlist.add(watchlistInputData);
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

}
