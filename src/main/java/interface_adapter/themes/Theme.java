package interface_adapter.themes;

import java.awt.Color;

/**
 * Represents a UI theme with a name, background color, and foreground color.
 */
public class Theme {
    private final String name;
    private final Color background;
    private final Color foreground;

    /**
     * Constructs a Theme.
     *
     * @param name the theme name
     * @param background the background color
     * @param foreground the foreground color
     */
    public Theme(String name, Color background, Color foreground) {
        this.name = name;
        this.background = background;
        this.foreground = foreground;
    }

    /**
     * Returns the theme name.
     *
     * @return the name of the theme
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the background color of the theme.
     *
     * @return the background color
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Returns the foreground color of the theme.
     *
     * @return the foreground color
     */
    public Color getForeground() {
        return foreground;
    }
}
