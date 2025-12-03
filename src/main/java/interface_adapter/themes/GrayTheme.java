package interface_adapter.themes;

import java.awt.Color;

/**
 * A gray theme with light gray background and dark gray foreground colors.
 */
public class GrayTheme extends Theme {

    // Background color components
    private static final int BG_R = 235;
    private static final int BG_G = 235;
    private static final int BG_B = 235;

    // Foreground color components
    private static final int FG_R = 30;
    private static final int FG_G = 30;
    private static final int FG_B = 30;

    /**
     * Constructs a GrayTheme with predefined gray color values.
     */
    public GrayTheme() {
        super(
                "gray",
                new Color(BG_R, BG_G, BG_B),
                new Color(FG_R, FG_G, FG_B)
        );
    }
}
