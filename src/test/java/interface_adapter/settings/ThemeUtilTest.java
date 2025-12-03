package interface_adapter.settings;

import interface_adapter.themes.Theme;
import interface_adapter.themes.ThemeUtil;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThemeUtilTest {

    @Test
    void applyTheme_changesComponentColorsAndTitledBorder() throws Exception {
        // create a small component tree on EDT
        JPanel panel = new JPanel();
        JLabel label = new JLabel("lbl");
        JButton btn = new JButton("btn");
        JPanel inner = new JPanel();
        inner.setBorder(BorderFactory.createTitledBorder("Group"));
        inner.add(new JLabel("inner"));
        panel.add(label);
        panel.add(btn);
        panel.add(inner);

        Theme theme = new Theme("t", Color.PINK, Color.BLUE) {}; // anon theme

        // apply theme on EDT
        SwingUtilities.invokeAndWait(() -> ThemeUtil.applyTheme(panel, theme));

        // All components should have new foreground/background
        List<Component> all = ThemeUtil.getAllComponents(panel);
        assertTrue(all.size() >= 3);

        // Check panel, label, button colors
        assertEquals(theme.getBackground(), panel.getBackground());
        assertEquals(theme.getForeground(), label.getForeground());

        // TitledBorder title color changed
        assertTrue(inner.getBorder() instanceof TitledBorder);
        TitledBorder tb = (TitledBorder) inner.getBorder();
        // TitledBorder's getTitleColor may be null in some LAFs; still ensure no exception and color set
        assertEquals(theme.getForeground(), tb.getTitleColor());
    }
}

