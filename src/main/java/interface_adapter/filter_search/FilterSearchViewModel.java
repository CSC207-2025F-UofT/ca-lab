package interface_adapter.filter_search;

import entity.Stock;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * The View Model for the Filter Search View.
 */

public class FilterSearchViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private final String viewName = "Filter Search";

    private List<Stock> stocks;
    private String errorMessage;

    public String getViewName() { return viewName; }

    public List<Stock> getStocks() { return stocks; }

    public void setStocks(List<Stock> stocks) { this.stocks = stocks; }

    public String getErrorMessage() { return errorMessage; }

    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public void firePropertyChange() {
        support.firePropertyChange("filterSearch", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }
}
