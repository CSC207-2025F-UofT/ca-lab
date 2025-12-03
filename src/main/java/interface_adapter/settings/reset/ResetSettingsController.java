package interface_adapter.settings.reset;

import use_case.settings.reset.ResetSettingsInputBoundary;

/**
 * Controller for reset application settings.
 */
public class ResetSettingsController {
    private final ResetSettingsInputBoundary resetSettingsInteractor;

    public ResetSettingsController(ResetSettingsInputBoundary resetSettingsInteractor) {
        this.resetSettingsInteractor = resetSettingsInteractor;
    }

    /**
     * Executes the reset settings use case.
     */
    public void execute() {
        resetSettingsInteractor.execute();
    }
}
