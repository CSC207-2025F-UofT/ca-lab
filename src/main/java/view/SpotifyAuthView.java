package view;

import interface_adapter.spotify_auth.SpotifyAuthController;
import interface_adapter.spotify_auth.SpotifyAuthState;
import interface_adapter.spotify_auth.SpotifyAuthViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.ViewManagerModel;
import util.CallbackServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SpotifyAuthView extends JPanel implements ActionListener, PropertyChangeListener {
    public static final String viewName = "spotify auth";
    private final SpotifyAuthViewModel spotifyAuthViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private ViewManagerModel viewManagerModel;  // NEW
    private final JButton connectSpotifyButton;
    private final JLabel statusLabel;
    private SpotifyAuthController spotifyAuthController;
    private String currentUsername;

    public SpotifyAuthView(SpotifyAuthViewModel spotifyAuthViewModel, LoggedInViewModel loggedInViewModel) {
        this.spotifyAuthViewModel = spotifyAuthViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.spotifyAuthViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel();
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // Instructions Label with HTML for line breaks
        JLabel instructions = new JLabel("<html><center>" +
                "Click the button below to connect your Spotify account.<br>" +
                "Your browser will open and you'll be asked to authorize the app." +
                "</center></html>");
        instructions.setFont(new Font("Calibri", Font.BOLD, 30));
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel(" ");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setForeground(Color.BLUE);

        JPanel buttonPanel = new JPanel();
        connectSpotifyButton = new JButton("Connect to Spotify");
        connectSpotifyButton.setFont(new Font("Monospaced", Font.BOLD, 30));
        connectSpotifyButton.setBackground(new Color(30, 215, 96));
        connectSpotifyButton.setForeground(Color.WHITE);
        connectSpotifyButton.setFocusPainted(false);
        connectSpotifyButton.setPreferredSize(new Dimension(500, 200));
        buttonPanel.add(connectSpotifyButton);

        connectSpotifyButton.addActionListener(evt -> {
            if (evt.getSource().equals(connectSpotifyButton)) {
                connectToSpotify();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(40));
        this.add(title);
        this.add(Box.createVerticalStrut(20));

        JPanel instructionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        instructionsPanel.add(instructions);                // Add instructions in the center
        this.add(instructionsPanel);

        this.add(Box.createVerticalStrut(30));
        this.add(buttonPanel);
        this.add(Box.createVerticalStrut(20));
        this.add(statusLabel);
        this.add(Box.createVerticalGlue());
    }

    // NEW METHOD
    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        // Listen for when this view becomes active
        if (viewManagerModel != null) {
            viewManagerModel.addPropertyChangeListener(evt -> {
                if ("state".equals(evt.getPropertyName()) &&
                        viewName.equals(evt.getNewValue())) {
                    // This view just became active - reset it
                    resetView();
                }
            });
        }
    }

    // NEW METHOD
    private void resetView() {
        connectSpotifyButton.setEnabled(true);
        statusLabel.setText(" ");
        statusLabel.setForeground(Color.BLUE);
    }

    private void connectToSpotify() {
        connectSpotifyButton.setEnabled(false);
        statusLabel.setText("Opening browser...");

        new Thread(() -> {
            try {
                CallbackServer server = new CallbackServer();

                SwingUtilities.invokeLater(() -> {
                    String authUrl = spotifyAuthController.getAuthorizationUrl();
                    if (authUrl != null) {
                        try {
                            Desktop.getDesktop().browse(java.net.URI.create(authUrl));
                            statusLabel.setText("Waiting for authorization...");
                        } catch (Exception e) {
                            statusLabel.setText("Error: Could not open browser");
                            statusLabel.setForeground(Color.RED);
                            JOptionPane.showMessageDialog(this,
                                    "Could not open browser. Please visit:\n" + authUrl,
                                    "Browser Error",
                                    JOptionPane.ERROR_MESSAGE);
                            connectSpotifyButton.setEnabled(true);
                            server.stop();
                        }
                    } else {
                        statusLabel.setText("Error: Could not get authorization URL");
                        statusLabel.setForeground(Color.RED);
                        connectSpotifyButton.setEnabled(true);
                        server.stop();
                    }
                });

                String code = server.startAndWaitForCode(120);

                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Completing authorization...");
                    spotifyAuthController.execute(code, getCurrentUsername());
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Authorization timed out or failed");
                    statusLabel.setForeground(Color.RED);
                    connectSpotifyButton.setEnabled(true);

                    if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                        JOptionPane.showMessageDialog(this,
                                "Authorization timed out. Please try again.",
                                "Timeout",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Authorization failed: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }).start();
    }

    private String getCurrentUsername() {
        if (loggedInViewModel != null && loggedInViewModel.getState() != null) {
            String username = loggedInViewModel.getState().getUsername();
            if (username != null && !username.isEmpty()) {
                return username;
            }
        }
        return currentUsername != null ? currentUsername : "user";
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle other actions if needed
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SpotifyAuthState state = (SpotifyAuthState) evt.getNewValue();

        if (state.getAuthError() != null) {
            statusLabel.setText("Error: " + state.getAuthError());
            statusLabel.setForeground(Color.RED);
            connectSpotifyButton.setEnabled(true);
        }
    }

    public void setSpotifyAuthController(SpotifyAuthController controller) {
        this.spotifyAuthController = controller;
    }

    public String getViewName() {
        return viewName;
    }
}