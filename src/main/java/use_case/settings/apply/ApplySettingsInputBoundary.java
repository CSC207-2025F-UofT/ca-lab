package use_case.settings.apply;

/**
 * Input boundary for the save settings use case.
 */
public interface ApplySettingsInputBoundary {
    /**
     * Executes the save-settings use case with the provided input data.
     *
     * @param inputData the theme settings chosen by the user that should be saved and applied
     */
    void execute(ApplySettingsInputData inputData);
}
