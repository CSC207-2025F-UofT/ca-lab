package view;

import interface_adapter.spotify_auth.SpotifyAuthController;
import interface_adapter.spotify_auth.SpotifyAuthState;
import interface_adapter.spotify_auth.SpotifyAuthViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.ViewManagerModel;

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
    private ViewManagerModel viewManagerModel;
    private final JButton connectSpotifyButton;
    private final JLabel statusLabel;
    private SpotifyAuthController spotifyAuthController;

    public SpotifyAuthView(SpotifyAuthViewModel spotifyAuthViewModel,
                           LoggedInViewModel loggedInViewModel) {
        this.spotifyAuthViewModel = spotifyAuthViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.spotifyAuthViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel("Connect Your Spotify Account");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 18));

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
        instructionsPanel.add(instructions);
        this.add(instructionsPanel);

        this.add(Box.createVerticalStrut(30));
        this.add(buttonPanel);
        this.add(Box.createVerticalStrut(20));
        this.add(statusLabel);
        this.add(Box.createVerticalGlue());
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        if (viewManagerModel != null) {
            viewManagerModel.addPropertyChangeListener(evt -> {
                if ("state".equals(evt.getPropertyName()) &&
                        viewName.equals(evt.getNewValue())) {
                    resetView();
                }
            });
        }
    }

    private void resetView() {
        connectSpotifyButton.setEnabled(true);
        statusLabel.setText(" ");
        statusLabel.setForeground(Color.BLUE);
    }

    private void connectToSpotify() {
        connectSpotifyButton.setEnabled(false);
        statusLabel.setText("Starting authorization...");

        // Get username from logged in state
        String username = "user";
        if (loggedInViewModel != null && loggedInViewModel.getState() != null) {
            String stateUsername = loggedInViewModel.getState().getUsername();
            if (stateUsername != null && !stateUsername.isEmpty()) {
                username = stateUsername;
            }
        }

        // Trigger the authorization flow through the controller
        spotifyAuthController.startAuthorization(username);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle other actions if needed
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        SpotifyAuthState state = spotifyAuthViewModel.getState();

        switch (propertyName) {
            case "authUrlReady":
                // Open browser when URL is ready
                try {
                    Desktop.getDesktop().browse(java.net.URI.create(state.getAuthorizationUrl()));
                    statusLabel.setText(state.getStatusMessage());
                } catch (Exception e) {
                    statusLabel.setText("Error: Could not open browser");
                    statusLabel.setForeground(Color.RED);
                    connectSpotifyButton.setEnabled(true);
                    JOptionPane.showMessageDialog(this,
                            "Could not open browser. Please visit:\n" + state.getAuthorizationUrl(),
                            "Browser Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "statusUpdate":
                statusLabel.setText(state.getStatusMessage());
                break;

            case "authSuccess":
                statusLabel.setText(state.getStatusMessage());
                statusLabel.setForeground(new Color(30, 215, 96));
                // View will switch automatically via ViewManagerModel
                break;

            case "authError":
                statusLabel.setText("Error: " + state.getAuthError());
                statusLabel.setForeground(Color.RED);
                connectSpotifyButton.setEnabled(true);
                JOptionPane.showMessageDialog(this,
                        state.getAuthError(),
                        "Authorization Error",
                        JOptionPane.ERROR_MESSAGE);
                break;

            default:
                // Handle legacy property changes
                if (state.getAuthError() != null && !state.getAuthError().isEmpty()) {
                    statusLabel.setText("Error: " + state.getAuthError());
                    statusLabel.setForeground(Color.RED);
                    connectSpotifyButton.setEnabled(true);
                }
                break;
        }
    }

    public void setSpotifyAuthController(SpotifyAuthController controller) {
        this.spotifyAuthController = controller;
    }

    public String getViewName() {
        return viewName;
    }
}