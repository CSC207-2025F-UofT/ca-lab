package interface_adapter.filter_search;

import entity.Stock;
import use_case.filter_search.FilterSearchOutputBoundary;
import use_case.filter_search.FilterSearchResponse;
import java.util.List;
/**
 * The Presenter for the Filter Search Use Case.
 */

public class FilterSearchPresenter implements FilterSearchOutputBoundary{
    private final FilterSearchViewModel filterSearchViewModel;

    public FilterSearchPresenter(FilterSearchViewModel filterSearchViewModel) {
        this.filterSearchViewModel = filterSearchViewModel;
    }

    @Override
    public void prepareSuccessView(FilterSearchResponse response) {
        List<Stock> stocks = response.getStocks();
        System.out.println("PRESENTER: response stocks = " +
                (stocks == null ? "null" : stocks.size()));

        filterSearchViewModel.setStocks(stocks);

        filterSearchViewModel.setErrorMessage(null);
        filterSearchViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String message) {
        System.out.println("PRESENTER: fail view, message = " + message);
        filterSearchViewModel.setStocks(null);
        filterSearchViewModel.setErrorMessage(message);
        filterSearchViewModel.firePropertyChange();
    }

}
