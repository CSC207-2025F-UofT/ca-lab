package interface_adapter.settings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel responsible for storing and telling about changes to the selected theme.
 */
public class ThemeViewModel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    // default
    private String theme = "Light";

    /**
     * Sets the active theme and notifies observers.
     *
     * @param theme updated theme name
     */
    public void setTheme(String theme) {
        this.theme = theme;
        pcs.firePropertyChange("theme", null, theme);
    }

    /**
     * Returns the active theme.
     *
     * @return the theme name
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Registers a PropertyChangeListener to observe theme changes.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
