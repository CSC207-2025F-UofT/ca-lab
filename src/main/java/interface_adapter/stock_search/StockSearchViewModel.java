package interface_adapter.stock_search;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class StockSearchViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String infoText;
    private String errorMessage;
    private boolean loading;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this);
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
