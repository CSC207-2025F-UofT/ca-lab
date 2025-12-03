package use_case.settings.apply;

/**
 * Output data for reset settings.
 */
public interface ApplySettingsOutputBoundary {
    /**
     * Prepares the view to display a successful save.
     *
     * @param outputData the theme that was successfully saved
     */
    void prepareSuccessView(ApplySettingsOutputData outputData);

    /**
     * Prepares the view to display an error that occurred while attempting
     * to save settings.
     *
     * @param errorMessage the error message to present to the user
     */
    void prepareFailView(String errorMessage);
}
