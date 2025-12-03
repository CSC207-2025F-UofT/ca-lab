package interface_adapter.themes;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

/**
 * Utility class for applying a theme.
 */
public class ThemeUtil {

    private ThemeUtil() {
        // nothing
    }

    /**
     * Applies the given theme to the provided the components.
     * @param root  the container
     * @param theme the theme to apply
     */
    public static void applyTheme(Container root, Theme theme) {
        List<Component> all = getAllComponents(root);
        all.add(root);

        for (Component c : all) {

            c.setBackground(theme.getBackground());
            c.setForeground(theme.getForeground());

            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                if (jc.getBorder() instanceof TitledBorder) {
                    TitledBorder tb = (TitledBorder) jc.getBorder();
                    tb.setTitleColor(theme.getForeground());
                }
            }
        }
        root.repaint();
    }

    /**
     * Returns a list of all components contained within the given container,
     * recursively including components inside nested containers.
     *
     * @param container the container from which to collect components
     * @return a list of all descendant components
     */
    public static List<Component> getAllComponents(Container container) {
        List<Component> list = new ArrayList<>();
        for (Component comp : container.getComponents()) {
            list.add(comp);
            if (comp instanceof Container) {
                list.addAll(getAllComponents((Container) comp));
            }
        }
        return list;
    }
}
