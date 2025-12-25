package interface_adapter.view_pet_details;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ViewPetDetailsViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private ViewPetDetailsState state = new ViewPetDetailsState();

    public void setState(ViewPetDetailsState state) {
        this.state = state;
        support.firePropertyChange("state", null, state);
    }

    public ViewPetDetailsState getState() {
        return state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
