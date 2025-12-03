package interface_adapter.settings;

import interface_adapter.themes.Theme;
import interface_adapter.themes.ThemeManager;
import interface_adapter.themes.ThemedView;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicReference;

class ThemeManagerTest {

    static class RecordingView implements ThemedView {
        final AtomicReference<Theme> last = new AtomicReference<>();
        @Override
        public void applyTheme(Theme theme) {
            last.set(theme);
        }
    }

    @Test
    void registerThemeAndNames() {
        ThemeManager mgr = new ThemeManager();
        // default includes light & dark
        assertTrue(mgr.getThemeNames().contains("light"));
        assertTrue(mgr.getThemeNames().contains("dark"));
    }

    @Test
    void registerView_appliesActiveThemeImmediately() {
        ThemeManager mgr = new ThemeManager();
        RecordingView v = new RecordingView();

        // change activeTheme first
        mgr.setTheme("dark");
        assertEquals("dark", mgr.getActiveTheme().getName());

        // register view -> should immediately receive active theme
        mgr.registerView(v);
        assertNotNull(v.last.get());
        assertEquals("dark", v.last.get().getName());
    }

    @Test
    void setTheme_caseInsensitiveAndNotifyAll() {
        ThemeManager mgr = new ThemeManager();
        RecordingView v1 = new RecordingView();
        RecordingView v2 = new RecordingView();

        mgr.registerView(v1);
        mgr.registerView(v2);

        mgr.setTheme("DaRk"); // case-insensitive
        assertEquals("dark", mgr.getActiveTheme().getName());

        assertEquals("dark", v1.last.get().getName());
        assertEquals("dark", v2.last.get().getName());
    }

    @Test
    void applyThemeToAllViews_callsEveryRegisteredView() {
        ThemeManager mgr = new ThemeManager();
        RecordingView v1 = new RecordingView();
        RecordingView v2 = new RecordingView();
        mgr.registerView(v1);
        mgr.registerView(v2);

        // change to  blue if present; fallback to "light"
        String any = mgr.getThemeNames().stream().findFirst().orElse("light");
        mgr.setTheme(any);
        mgr.applyThemeToAllViews();

        assertNotNull(v1.last.get());
        assertNotNull(v2.last.get());
        assertEquals(mgr.getActiveTheme().getName(), v1.last.get().getName());
    }
}

