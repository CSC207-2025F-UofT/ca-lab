package interface_adapter.filter_search;

import entity.Stock;
import java.util.List;

public class FilterSearchState {
    // List of stocks to display in the table
    private List<Stock> stocks;

    // Error message to show in a dialog (null or empty if no error)
    private String errorMessage;

    public FilterSearchState() {
    }

    // Optional copy constructor (useful if your ViewModel copies state)
    public FilterSearchState(FilterSearchState copy) {
        this.stocks = copy.stocks;
        this.errorMessage = copy.errorMessage;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
