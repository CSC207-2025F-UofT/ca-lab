package interface_adapter.earnings_history;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EarningsHistoryViewModel extends ViewModel {

    public static final String TITLE_LABEL = "Company Earnings";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private EarningsHistoryState state = new EarningsHistoryState();

    public EarningsHistoryViewModel() {
        super("earnings history");
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public EarningsHistoryState getState() {
        return state;
    }

    public void setState(EarningsHistoryState state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }
}
