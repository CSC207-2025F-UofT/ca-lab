package interface_adapter.themes;

import java.awt.Color;

/**
 * A dark theme with dark background and light foreground colors.
 */
public class DarkTheme extends Theme {

    // Background color components
    private static final int BG_R = 45;
    private static final int BG_G = 45;
    private static final int BG_B = 45;

    // Foreground color components
    private static final int FG_R = 230;
    private static final int FG_G = 230;
    private static final int FG_B = 230;

    /**
     * Constructs a DarkTheme with predefined dark color values.
     */
    public DarkTheme() {
        super(
                "dark",
                new Color(BG_R, BG_G, BG_B),
                new Color(FG_R, FG_G, FG_B)
        );
    }
}
