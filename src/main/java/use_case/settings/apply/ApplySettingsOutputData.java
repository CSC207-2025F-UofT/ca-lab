package use_case.settings.apply;

/**
 * Output data for reset settings.
 */
public class ApplySettingsOutputData {
    private final String theme;

    public ApplySettingsOutputData(String theme) {
        this.theme = theme;
    }

    /**
     * Returns the theme name to be switched to.
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }
}
