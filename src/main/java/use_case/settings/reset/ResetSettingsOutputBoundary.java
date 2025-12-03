package use_case.settings.reset;

/**
 * The output boundary for the Reset Settings use case.
 */
public interface ResetSettingsOutputBoundary {
    /**
     * Prepares the success view.
     * @param outputData information about the theme they want it to be
     */
    void prepareSuccessView(ResetSettingsOutputData outputData);
}
