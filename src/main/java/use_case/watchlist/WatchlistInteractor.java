package use_case.watchlist;


import entity.User;

public class WatchlistInteractor implements WatchlistInputBoundary {
    private final WatchlistUserDataAccessInterface wlUserDataAccessObject;
    private final WatchlistOutputBoundary watchlistPresenter;

    public WatchlistInteractor(WatchlistUserDataAccessInterface wlUserDataAccessInterface,
                               WatchlistOutputBoundary watchlistOutputBoundary) {
        this.wlUserDataAccessObject = wlUserDataAccessInterface;
        this.watchlistPresenter = watchlistOutputBoundary;
    }

    public void execute(WatchlistInputData watchlistInputData) {
        String username = watchlistInputData.getUsername();

        if (!wlUserDataAccessObject.existsByName(username)) {
            watchlistPresenter.prepareFailView("User does not exist.");
            return;
        }
        User user = wlUserDataAccessObject.get(username);

        user.getWatchlist().add(watchlistInputData.getWatchlistInputData());

        wlUserDataAccessObject.save(user);

        watchlistPresenter.prepareSuccessView(username,
                new WatchlistOutputData(user.getWatchlist())
        );
    }

}
