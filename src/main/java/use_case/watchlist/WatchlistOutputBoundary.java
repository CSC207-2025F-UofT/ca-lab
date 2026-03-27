package use_case.watchlist;

public interface WatchlistOutputBoundary {

    void prepareSuccessView(String username, WatchlistOutputData outputData);

    void prepareFailView(String errorMessage);
}
