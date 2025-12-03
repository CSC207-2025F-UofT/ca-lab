package interface_adapter.settings.apply;

import interface_adapter.settings.SettingsState;
import use_case.settings.apply.ApplySettingsInputBoundary;
import use_case.settings.apply.ApplySettingsInputData;

/**
 * The controller for applying the chosen settings to the game.
 */
public class ApplySettingsController {
    private final ApplySettingsInputBoundary applySettingsInputBoundary;

    public ApplySettingsController(ApplySettingsInputBoundary saveSettingsInteractor) {
        this.applySettingsInputBoundary = saveSettingsInteractor;
    }

    /**
     * Executes the save settings use case using the current state of the view.
     * @param state the current SettingsState from the settings view
     */
    public void execute(SettingsState state) {
        ApplySettingsInputData data =
                new ApplySettingsInputData(state.getTheme());
        applySettingsInputBoundary.execute(data);
    }
}
