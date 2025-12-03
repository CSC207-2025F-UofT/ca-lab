package use_case.settings.apply;

/**
 * The DAO for the save settings use case
 */
public class ApplySettingsInputData {
    private final String theme;

    public ApplySettingsInputData(String theme) {
        this.theme = theme;
    }

    /**
     * Returns the theme selected by the user.
     *
     * @return the name of the selected theme
     */
    public String getTheme() {
        return theme;
    }
}
