package use_case.watchlist;

import entity.User;
import org.json.JSONObject;

import java.util.ArrayList;

public class RemoveWatchlistInteractor implements RemoveWatchlistInputBoundary {

    private final WatchlistUserDataAccessInterface removeDataAccessObject;
    private final WatchlistOutputBoundary removePresenter;

    public RemoveWatchlistInteractor(WatchlistUserDataAccessInterface wlUserDataAccessInterface,
                               WatchlistOutputBoundary watchlistOutputBoundary) {
        this.removeDataAccessObject = wlUserDataAccessInterface;
        this.removePresenter = watchlistOutputBoundary;
    }

    public void execute(RemoveWatchlistInputData watchlistInputData) {
        String username = watchlistInputData.getUsername();
        JSONObject itemToRemove = watchlistInputData.getItem();

        if (!removeDataAccessObject.existsByName(watchlistInputData.getUsername())) {
            removePresenter.prepareFailView("User does not exist.");
            return;
        }
        User user = removeDataAccessObject.get(username);

        ArrayList<JSONObject> watchlist = user.getWatchlist();
        if (itemToRemove != null) {
            String keyToRemove = itemToRemove.keySet().iterator().next();
            watchlist.removeIf(obj -> obj != null && obj.has(keyToRemove));


        removeDataAccessObject.save(user);

        removePresenter.prepareSuccessView(username,
                new WatchlistOutputData(user.getWatchlist()));
        }
    }
}
