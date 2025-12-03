package interface_adapter.themes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages available themes and applies the active theme to registered views.
 */
public class ThemeManager {
    private final Map<String, Theme> themes = new HashMap<>();
    private Theme activeTheme;

    private final List<ThemedView> registeredViews = new ArrayList<>();

    /**
     * Constructs a ThemeManager and registers default themes.
     */
    public ThemeManager() {
        registerTheme(new LightTheme());
        registerTheme(new DarkTheme());
        registerTheme(new BlueTheme());
        registerTheme(new GoldTheme());
        registerTheme(new GrayTheme());
        activeTheme = themes.get("light");
    }

    /**
     * Registers a theme to the manager.
     *
     * @param theme the theme to register
     */
    public void registerTheme(Theme theme) {

        themes.put(theme.getName(), theme);
    }

    /**
     * Returns the names of all registered themes.
     *
     * @return a set of theme names
     */
    public Set<String> getThemeNames() {
        return themes.keySet();
    }

    /**
     * Sets the active theme if it exists.
     *
     * @param themeName the name of the theme to activate
     */
    public void setTheme(String themeName) {
        if (themeName != null) {
            String key = themeName.toLowerCase();
            if (themes.containsKey(key)) {
                activeTheme = themes.get(key);
                applyThemeToAllViews();
            }
        }

    }

    /**
     * Returns the currently active theme.
     *
     * @return the active theme
     */
    public Theme getActiveTheme() {
        return activeTheme;
    }

    /**
     * Registers a view so it receives theme updates.
     *
     * @param view the view to register
     */
    public void registerView(ThemedView view) {
        if (!registeredViews.contains(view)) {
            registeredViews.add(view);
        }
        // apply current theme to the view immediately
        if (activeTheme != null) {
            view.applyTheme(activeTheme);
        }
    }

    /**
     * Applies the active theme to all registered views.
     */
    public void applyThemeToAllViews() {
        for (ThemedView v : registeredViews) {
            v.applyTheme(activeTheme);
        }
    }
}
