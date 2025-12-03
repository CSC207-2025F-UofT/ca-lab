package interface_adapter.themes;

import java.awt.Color;

/**
 * A theme with blue-based background and foreground colors.
 */
public class BlueTheme extends Theme {

    // Background color components
    private static final int BG_R = 240;
    private static final int BG_G = 245;
    private static final int BG_B = 255;

    // Foreground color components
    private static final int FG_R = 15;
    private static final int FG_G = 30;
    private static final int FG_B = 60;

    /**
     * Constructs a BlueTheme with predefined blue color values.
     */
    public BlueTheme() {
        super(
                "blue",
                new Color(BG_R, BG_G, BG_B),
                new Color(FG_R, FG_G, FG_B)
        );
    }
}
