package use_case.watchlist;

public interface WatchlistOutputBoundary {

    void prepareSuccessView(WatchlistOutputData outputData);

    void prepareFailView(String errorMessage);
}
