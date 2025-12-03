package use_case.settings.reset;

/**
 * Output data for reset settings.
 */
public class ResetSettingsOutputData {
    private final String defaultTheme;

    public ResetSettingsOutputData(String defaultTheme) {
        this.defaultTheme = defaultTheme;
    }

    /**
     * Returns the default theme name to be restored.
     * @return the default theme
     */
    public String getDefaultTheme() {
        return defaultTheme;
    }
}
