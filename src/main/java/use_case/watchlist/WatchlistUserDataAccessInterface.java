package use_case.watchlist;

import entity.User;

public interface WatchlistUserDataAccessInterface {
    User get(String username);

    void save(User user);

    boolean existsByName(String username);
}
