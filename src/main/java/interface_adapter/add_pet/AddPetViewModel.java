package interface_adapter.add_pet;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AddPetViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String statusMessage;

    public void setStatusMessage(String message) {
        String old = this.statusMessage;
        this.statusMessage = message;
        support.firePropertyChange("addPetStatus", old, this.statusMessage);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public String getViewName() {
        return "Add Pet";
    }
}

