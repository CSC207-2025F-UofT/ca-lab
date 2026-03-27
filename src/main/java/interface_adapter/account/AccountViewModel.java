package interface_adapter.account;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AccountViewModel {
    private AccountState state = new AccountState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public AccountState getState() {
        return state;
    }

    public void setState(AccountState newState) {
        this.state = newState;
        support.firePropertyChange("state", null, newState);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChange() {
        support.firePropertyChange("watchlist", null, state.getWatchlist());
    }


}
