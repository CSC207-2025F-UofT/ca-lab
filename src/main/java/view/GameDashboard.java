package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.themes.Theme;
import interface_adapter.themes.ThemeManager;
import interface_adapter.themes.ThemeUtil;
import interface_adapter.themes.ThemedView;

import javax.swing.*;
import java.awt.*;

/**
 * The view for displaying the game dashboard.
 */
public class GameDashboard extends JPanel implements ThemedView {

    private final ViewManagerModel viewManagerModel;
    private String currentUser = null; // null = not logged in

    private final JPanel headerButtons;
    private final JLabel userLabel;

    private ChooseFavPokemonView chooseFavPokemonView = null;
    public GameDashboard(ViewManagerModel viewManagerModel, ThemeManager themeManager) {

        this.viewManagerModel = viewManagerModel;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 255));

        // ===== COLOUR THEME CHANGE =====
        themeManager.registerView(this);
        applyTheme(themeManager.getActiveTheme());

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Pokénator Dashboard", JLabel.LEFT);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        headerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerButtons.setOpaque(false);

        JButton loginBtn = createHeaderButton("Login");
        JButton signupBtn = createHeaderButton("Create Account");
        headerButtons.add(loginBtn);
        headerButtons.add(signupBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(headerButtons, BorderLayout.EAST);

        userLabel = new JLabel("", JLabel.RIGHT);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, BorderLayout.CENTER);

        // ===== MAIN BUTTONS =====
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        buttonPanel.setBackground(getBackground());

        JButton playGameBtn = createStyledButton("Play Game");
        JButton leaderboardBtn = createStyledButton("Leaderboard");
        JButton profileBtn = createStyledButton("Profile");
        JButton settingsBtn = createStyledButton("Settings");
        JButton aboutBtn = createStyledButton("About");
        JButton exitBtn = createStyledButton("Exit");

        buttonPanel.add(playGameBtn);
        buttonPanel.add(leaderboardBtn);
        buttonPanel.add(profileBtn);
        buttonPanel.add(settingsBtn);
        buttonPanel.add(aboutBtn);
        buttonPanel.add(exitBtn);

        // ===== FOOTER =====
        JLabel footerLabel = new JLabel("© Pokénator by Pibble Nation", JLabel.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footerLabel.setForeground(Color.DARK_GRAY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(footerLabel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====
        loginBtn.addActionListener(e -> {
            viewManagerModel.setState("log in");
            viewManagerModel.firePropertyChange();
        });

        signupBtn.addActionListener(e -> {
            viewManagerModel.setState("sign up");
            viewManagerModel.firePropertyChange();
        });

        playGameBtn.addActionListener(e -> {
            Object[] options = {"Mystery Pokémon", "Guess My Pokémon"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Pick a mode:",
                    "Play Game",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (choice == 0) {
                viewManagerModel.setState("mysterypokemon");
                viewManagerModel.firePropertyChange();
            } else if (choice == 1) {
                viewManagerModel.setState("akinator");
                viewManagerModel.firePropertyChange();
            }
        });

        leaderboardBtn.addActionListener(e -> {
            viewManagerModel.setState("leaderboard");
            viewManagerModel.firePropertyChange();
        });
        profileBtn.addActionListener(e -> {
            if (currentUser == null) {
                showMessage("Please log in to view your profile.");
            } else {
                viewManagerModel.setState("User Profile");
                viewManagerModel.firePropertyChange();
            }
        });

        settingsBtn.addActionListener(e -> {
            viewManagerModel.setState("settings");
            viewManagerModel.firePropertyChange();
        });
        aboutBtn.addActionListener(e -> showMessage("Pokénator by Pibble Nation!"));
        exitBtn.addActionListener(e -> System.exit(0));
    }

    // ===== PUBLIC METHODS =====
    public void setUser(String username) {
        this.currentUser = username;
        refreshHeader();
    }

    public String getCurrentUser() {
        return this.currentUser;
    }

    public void logout() {
        this.currentUser = null;
        refreshHeader();
    }

    private void refreshHeader() {
        headerButtons.removeAll();
        if (currentUser == null) {
            // Guest view
            JButton loginBtn = createHeaderButton("Login");
            JButton signupBtn = createHeaderButton("Create Account");
            loginBtn.addActionListener(e -> {
                viewManagerModel.setState("log in");
                viewManagerModel.firePropertyChange();
            });
            signupBtn.addActionListener(e -> {
                viewManagerModel.setState("sign up");
                viewManagerModel.firePropertyChange();
            });
            headerButtons.add(loginBtn);
            headerButtons.add(signupBtn);
            userLabel.setText("");
        } else {
            // Logged-in view
            JLabel nameLabel = new JLabel("Logged in as: " + currentUser);
//            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            JButton logoutBtn = createHeaderButton("Logout");
            logoutBtn.addActionListener(e -> {
                logout();
                viewManagerModel.setState("dashboard");
                viewManagerModel.firePropertyChange();
            });
            headerButtons.add(nameLabel);
            headerButtons.add(logoutBtn);
        }
        revalidate();
        repaint();
    }

    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(100, 149, 237));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(230, 240, 255));
        button.setForeground(Color.DARK_GRAY);
        button.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 240), 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void applyTheme(Theme theme) {
        ThemeUtil.applyTheme(this, theme);
    }

    public String getViewName() {
        return "dashboard";
    }

    public void setCFPV(ChooseFavPokemonView CFPV) {
        this.chooseFavPokemonView = CFPV;
    }
}
