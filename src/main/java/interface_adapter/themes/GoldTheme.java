package interface_adapter.themes;

import java.awt.Color;

/**
 * A gold theme with warm background and dark brown foreground colors.
 */
public class GoldTheme extends Theme {

    // Background color components
    private static final int BG_R = 250;
    private static final int BG_G = 245;
    private static final int BG_B = 230;

    // Foreground color components
    private static final int FG_R = 60;
    private static final int FG_G = 45;
    private static final int FG_B = 30;

    /**
     * Constructs a GoldTheme with predefined gold color values.
     */
    public GoldTheme() {
        super(
                "gold",
                new Color(BG_R, BG_G, BG_B),
                new Color(FG_R, FG_G, FG_B)
        );
    }
}
