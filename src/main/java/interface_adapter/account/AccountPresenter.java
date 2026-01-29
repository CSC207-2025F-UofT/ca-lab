package interface_adapter.account;

import use_case.watchlist.WatchlistOutputBoundary;
import use_case.watchlist.WatchlistOutputData;

import java.util.ArrayList;

public class AccountPresenter implements WatchlistOutputBoundary {

    private final AccountViewModel viewModel;

    public AccountPresenter(AccountViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void prepareSuccessView(WatchlistOutputData watchlistOutputData) {
        AccountState state = viewModel.getState();
        state.setWatchlist(watchlistOutputData.getWatchlist());
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    public void prepareFailView(String errorMessage) {
        viewModel.getState().setWatchlist(new ArrayList<>());
        viewModel.firePropertyChange();
    }

}

