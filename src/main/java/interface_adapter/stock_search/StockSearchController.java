package interface_adapter.stock_search;

import use_case.stock_search.StockSearchInputBoundary;
import use_case.stock_search.StockSearchRequestModel;

public class StockSearchController {

    private final StockSearchInputBoundary interactor;
    private final StockSearchViewModel viewModel;

    public StockSearchController(StockSearchInputBoundary interactor,
                                 StockSearchViewModel viewModel) {
        this.interactor = interactor;
        this.viewModel = viewModel;
    }

    public void search(String symbol) {
        viewModel.setLoading(true);
        viewModel.setErrorMessage(null);
        viewModel.firePropertyChanged();

        new Thread(() -> {
            StockSearchRequestModel request = new StockSearchRequestModel(symbol);
            interactor.execute(request);
        }).start();
    }
}
