package interface_adapter.settings;

import interface_adapter.ViewManagerModel;
import interface_adapter.settings.apply.ApplySettingsPresenter;
import interface_adapter.themes.ThemeManager;
import org.junit.jupiter.api.Test;
import use_case.settings.apply.ApplySettingsOutputData;

import static org.junit.jupiter.api.Assertions.*;

class ApplySettingsPresenterTest {

    // A tiny Fake ThemeManager to observe theme changes
    static class FakeThemeManager extends ThemeManager {
        String lastSet = null;

        @Override
        public void setTheme(String themeName) {
            lastSet = themeName;
            super.setTheme(themeName);
        }
    }

    @Test
    void prepareSuccessView_updatesViewModelAndGlobalThemeAndViewManager() {
        // Arrange
        ViewManagerModel viewManager = new ViewManagerModel();
        SettingsViewModel settingsVm = new SettingsViewModel();
        FakeThemeManager themeManager = new FakeThemeManager();

        ApplySettingsPresenter presenter =
                new ApplySettingsPresenter(viewManager, settingsVm, themeManager);

        // sanity: initial state not "settings"
        assertNotEquals("settings", viewManager.getState());

        // Act
        presenter.prepareSuccessView(new ApplySettingsOutputData("dark"));

        // Assert - SettingsViewModel updated
        SettingsState state = settingsVm.getState();
        assertEquals("dark", state.getTheme());
        assertNull(state.getErrorMessage());

        // Assert - ThemeManager setTheme called
        assertEquals("dark", themeManager.lastSet);

        // Assert - view manager switched to settings
        assertEquals("settings", viewManager.getState());
    }

    @Test
    void prepareFailView_setsErrorMessageOnViewModelOnly() {
        // Arrange
        ViewManagerModel viewManager = new ViewManagerModel();
        SettingsViewModel settingsVm = new SettingsViewModel();
        FakeThemeManager themeManager = new FakeThemeManager();

        ApplySettingsPresenter presenter =
                new ApplySettingsPresenter(viewManager, settingsVm, themeManager);

        // Precondition: no error message
        assertNull(settingsVm.getState().getErrorMessage());

        // Act
        presenter.prepareFailView("oh no");

        // Assert - error message is set
        assertEquals("oh no", settingsVm.getState().getErrorMessage());

        // Assert - theme manager was not changed
        assertNull(themeManager.lastSet);

        // Assert - view manager did not change to settings
        assertNotEquals("settings", viewManager.getState());
    }
}
