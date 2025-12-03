package interface_adapter.settings;


/**
 * Represents the state of the Settings view, including the selected theme
 * and any error message that should be displayed.
 */
public class SettingsState {

    // The currently selected theme, the default theme is the light theme
    private String theme = "light";
    private String errorMessage;

    public SettingsState() {
        errorMessage = null;
    }

    // Copy constructor (to be safe)
    public SettingsState(SettingsState copy) {
        this.theme = copy.theme;
        errorMessage = null;
        this.errorMessage = copy.errorMessage;
    }

    /**
     * Returns the currently selected theme.
     *
     * @return the theme name
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Sets the theme for this state.
     *
     * @param theme the new theme name
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * Returns the current error message, if any.
     *
     * @return the error message, or null if none is set
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets an error message for this state.
     *
     * @param errorMessage the error message to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
