package interface_adapter.settings;

import interface_adapter.settings.reset.ResetSettingsPresenter;
import interface_adapter.themes.ThemeManager;
import org.junit.jupiter.api.Test;
import use_case.settings.reset.ResetSettingsOutputData;

import static org.junit.jupiter.api.Assertions.*;

class ResetSettingsPresenterTest {

    static class FakeThemeManager extends ThemeManager {
        String last = null;
        @Override
        public void setTheme(String themeName) {
            last = themeName;
            super.setTheme(themeName);
        }
    }

    @Test
    void prepareSuccess_setsDefaultThemeAndClearsError() {
        SettingsViewModel vm = new SettingsViewModel();
        FakeThemeManager tman = new FakeThemeManager();
        ResetSettingsPresenter presenter = new ResetSettingsPresenter(vm, tman);

        presenter.prepareSuccessView(new ResetSettingsOutputData("light"));

        assertEquals("light", vm.getState().getTheme());
        assertNull(vm.getState().getErrorMessage());
        assertEquals("light", tman.last);
    }
}

