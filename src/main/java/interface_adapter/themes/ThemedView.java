package interface_adapter.themes;

/**
 * Represents a view that supports applying a theme.
 */
public interface ThemedView {
    /**
     * Applies the given theme to the view.
     *
     * @param theme the theme to apply
     */
    void applyTheme(Theme theme);
}
