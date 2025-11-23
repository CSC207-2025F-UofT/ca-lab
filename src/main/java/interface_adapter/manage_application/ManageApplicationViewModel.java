package interface_adapter.manage_application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ManageApplicationViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private ManageApplicationState state = new ManageApplicationState();

    public ManageApplicationState getState() { return state; }

    public void setState(ManageApplicationState newState) {
        ManageApplicationState old = this.state;
        this.state = newState;
        support.firePropertyChange("state", old, newState);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }
}
