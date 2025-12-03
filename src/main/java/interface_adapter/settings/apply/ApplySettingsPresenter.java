package interface_adapter.settings.apply;

import interface_adapter.ViewManagerModel;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.themes.ThemeManager;
import use_case.settings.apply.ApplySettingsOutputBoundary;
import use_case.settings.apply.ApplySettingsOutputData;

/**
 * Presenter for saving settings. Updates the settings view model and applies
 * the selected theme globally.
 */
public class ApplySettingsPresenter implements ApplySettingsOutputBoundary {
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;
    private final ThemeManager themeManager;

    /**
     * Constructs a SaveSettingsPresenter.
     *
     * @param viewManagerModel the view manager
     * @param settingsViewModel the settings view model
     * @param themeManager the theme manager
     */

    public ApplySettingsPresenter(ViewManagerModel viewManagerModel,
                                  SettingsViewModel settingsViewModel,
                                  ThemeManager themeManager) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.themeManager = themeManager;
    }

    /**
     * Prepares the success view after settings are saved.
     *
     * @param theme the theme that was successfully selected
     */
    @Override
    public void prepareSuccessView(ApplySettingsOutputData outputData) {
        String theme = outputData.getTheme();
        // Update settings screen
        SettingsState state = settingsViewModel.getState();
        state.setTheme(theme);
        state.setErrorMessage(null);
        settingsViewModel.firePropertyChange();

        // GLOBAL THEME UPDATE
        themeManager.setTheme(theme);

        viewManagerModel.setState("settings");
        viewManagerModel.firePropertyChange();
    }

    /**
     * Prepares the fail view if an error occurs.
     *
     * @param errorMessage the error message
     */
    @Override
    public void prepareFailView(String errorMessage) {
        SettingsState state = settingsViewModel.getState();
        state.setErrorMessage(errorMessage);
        settingsViewModel.firePropertyChange();
    }
}