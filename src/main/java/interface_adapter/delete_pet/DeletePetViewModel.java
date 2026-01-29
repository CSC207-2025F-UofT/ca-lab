package interface_adapter.delete_pet;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DeletePetViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setPetPreviewMessage(String msg) {
        support.firePropertyChange("petPreview", null, msg);
    }

    public void setWarningMessage(String msg) {
        support.firePropertyChange("deleteWarning", null, msg);
    }

    public void setStatusMessage(String msg) {
        support.firePropertyChange("deleteStatus", null, msg);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public String getViewName() {
        return "Delete Pet";
    }
}

