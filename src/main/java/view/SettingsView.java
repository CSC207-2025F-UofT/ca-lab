package view;

// standard Java imports
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// special imports
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

// project imports
import interface_adapter.back.BackController;
import interface_adapter.settings.reset.ResetSettingsController;
import interface_adapter.settings.apply.ApplySettingsController;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.themes.Theme;
import interface_adapter.themes.ThemeManager;
import interface_adapter.themes.ThemeUtil;
import interface_adapter.themes.ThemedView;

/**
 * The settings screen, allowing the user to select themes and update
 * application preferences. This view displays the available themes,
 * provides controls to apply, reset, or go back, and updates itself
 * when a theme is applied globally.
 */
public class SettingsView extends JPanel implements ActionListener, ThemedView {

    private final String viewName;

    private final SettingsViewModel settingsViewModel;

    private BackController backController;
    private ResetSettingsController resetController;
    private ApplySettingsController applySettingsController;

    private final JToggleButton lightModeButton;
    private final JToggleButton darkModeButton;
    private final JButton saveButton;
    private final JButton resetButton;
    private final JButton backButton;
    private final JComboBox<String> themeSelector;

    /**
     * Creates the settings view and initializes all UI components.
     *
     * @param settingsViewModel the view model backing this view
     * @param themeManager the global theme manager
     */
    public SettingsView(SettingsViewModel settingsViewModel,
                        ThemeManager themeManager) {

        this.settingsViewModel = settingsViewModel;
        setLayout(new BorderLayout(10, 10));

        // HEADER
        JLabel title = new JLabel("Settings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // THEME PANEL
        JPanel themePanel = new JPanel(new GridLayout(0, 1, 0, 15));
        themePanel.setBorder(BorderFactory.createTitledBorder("Colour Theme"));

        JPanel buttonRow = new JPanel(new FlowLayout());

        ButtonGroup themeGroup = new ButtonGroup();

        lightModeButton = new JToggleButton("Light Mode");
        darkModeButton = new JToggleButton("Dark Mode");

        themeGroup.add(lightModeButton);
        themeGroup.add(darkModeButton);

        lightModeButton.addActionListener(this);
        darkModeButton.addActionListener(this);

        buttonRow.add(lightModeButton);
        buttonRow.add(darkModeButton);
        themePanel.add(buttonRow);

        // Theme selector (shows available theme names)
        themeSelector = new JComboBox<>(themeManager.getThemeNames().toArray(new String[0]));
        themeSelector.setSelectedItem(settingsViewModel.getState().getTheme());
        themePanel.add(new JLabel("All Available Themes:"));
        themePanel.add(themeSelector);

        add(themePanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottom = new JPanel(new FlowLayout());
        saveButton = new JButton("Apply");
        resetButton = new JButton("Reset");
        backButton = new JButton("Back");

        saveButton.addActionListener(this);
        resetButton.addActionListener(this);
        backButton.addActionListener(this);

        bottom.add(resetButton);
        bottom.add(saveButton);
        bottom.add(backButton);

        add(bottom, BorderLayout.SOUTH);

        // set an initial toggle
        String initial = settingsViewModel.getState().getTheme();
        if ("dark".equalsIgnoreCase(initial)) darkModeButton.setSelected(true);
        else lightModeButton.setSelected(true);

        // Register to receive theme updates and apply current theme immediately
        themeManager.registerView(this);
        viewName = "settings";
    }

    // Controllers

    /**
     * Assigns the controller for the back button.
     *
     * @param accessController controller to access another view
     */
    public void setAccessSettingsController(BackController accessController) {
        this.backController = accessController;
    }

    /**
     * Assigns the controller for the reset button.
     *
     * @param resetController the reset settings controller
     */
    public void setResetSettingsController(ResetSettingsController resetController) {
        this.resetController = resetController;
    }

    /**
     * Assigns the controller for the save button.
     *
     * @param saveController the save and apply settings controller
     */
    public void setSaveSettingsController(ApplySettingsController saveController) {
        this.applySettingsController = saveController;
    }

    /**
     * Listens for user's actions.
     *
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object src = event.getSource();

        if (src == saveButton) {
            // prefer the combobox selection if set, otherwise the toggle
            String chosen = (String) themeSelector.getSelectedItem();
            if (chosen == null) {
                chosen = lightModeButton.isSelected() ? "light" : "dark";
            }
            SettingsState state = settingsViewModel.getState();
            state.setTheme(chosen.toLowerCase());

            if (applySettingsController != null) applySettingsController.execute(state);

        } else if (src == resetButton) {
            if (resetController != null) resetController.execute();
        } else if (src == backButton) {
            if (backController != null) backController.execute();
        } else if (src == lightModeButton || src == darkModeButton) {
            // update selector to match toggle
            if (lightModeButton.isSelected()) themeSelector.setSelectedItem("light");
            else if (darkModeButton.isSelected()) themeSelector.setSelectedItem("dark");
        }
    }

    /**
     * Applies a chosen theme to the settings screen.
     *
     * @param theme the theme to apply
     */
    @Override
    public void applyTheme(Theme theme) {
        // ThemeUtil will recolor everything in this panel
        ThemeUtil.applyTheme(this, theme);
        // ensure toggle selection matches active theme
        String name = theme.getName();
        if ("dark".equalsIgnoreCase(name)) {
            darkModeButton.setSelected(true);
            themeSelector.setSelectedItem("dark");
        } else {
            lightModeButton.setSelected(true);
            themeSelector.setSelectedItem("light");
        }
    }

    /**
     * Returns the view name.
     *
     * @return viewName
     */
    public String getViewName() {
        return viewName;
    }
}
