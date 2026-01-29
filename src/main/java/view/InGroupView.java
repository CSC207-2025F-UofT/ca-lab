package view;

import data_access.SpotifyDataAccessObject;
import entity.SpotifyUser;
import entity.UserTasteProfile;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.InGroupViewModel;
import interface_adapter.create_group.UserGroupState;
import interface_adapter.group_analytics.GroupAnalyticsController;
import interface_adapter.group_analytics.GroupAnalyticsViewModel;
import interface_adapter.sharedsong.SharedSongController;
import interface_adapter.sharedsong.SharedSongState;
import interface_adapter.sharedsong.SharedSongViewModel;
import se.michaelthelin.spotify.model_objects.specification.User;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;


public class InGroupView extends JPanel implements ActionListener, PropertyChangeListener {

    String viewName = "in group";
    private final ViewManagerModel viewManagerModel;
    private final InGroupViewModel inGroupViewModel;
    private final SharedSongViewModel sharedSongViewModel;
    private final GroupAnalyticsViewModel groupAnalyticsViewModel;

    private final JButton loggedIn;
    private final JLabel groupName;
    private final JPanel groupPanel;
    private final JButton sharedSong;
    private final JButton groupAnalytics;

    private SharedSongController sharedSongController = null;
    private GroupAnalyticsController groupAnalyticsController = null;

    public InGroupView(ViewManagerModel viewManagerModel,
                       InGroupViewModel inGroupViewModel,
                       SharedSongViewModel sharedSongViewModel,
                       GroupAnalyticsViewModel groupAnalyticsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.inGroupViewModel = inGroupViewModel;
        this.sharedSongViewModel = sharedSongViewModel;
        this.groupAnalyticsViewModel = groupAnalyticsViewModel;

        // Listen to changes in the InGroupViewModel
        this.inGroupViewModel.addPropertyChangeListener(this);

        final UserGroupState currentState = inGroupViewModel.getState();

        groupName = new JLabel();
        setGroupName(currentState.getGroupName());
        groupName.setFont(new Font("Century Schoolbook", Font.PLAIN, 18));
        groupName.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        groupPanel = new JPanel();
        setGroupPanel(currentState);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(groupName);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(groupPanel);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        loggedIn = new JButton("Main Page");
        buttons.add(loggedIn);
        sharedSong = new JButton("Shared Song");
        buttons.add(sharedSong);
        groupAnalytics = new JButton("Group Analytics");
        buttons.add(groupAnalytics);

        loggedIn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(loggedIn)) {
                            viewManagerModel.setState("logged in");
                            viewManagerModel.firePropertyChange();
                        }
                    }
                }
        );

        sharedSong.addActionListener(evt -> {
            if (!evt.getSource().equals(sharedSong)) {
                return;
            }

            if (sharedSongController == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Shared Song controller is not wired.",
                        "Internal Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            UserGroupState state = inGroupViewModel.getState();

            if (state.getSpotifyUser() == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Current user is missing. Try reconnecting Spotify.",
                        "Shared Song Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (state.getGroupUsers() == null || state.getGroupUsers().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No group members available for shared song.",
                        "Shared Song Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            sharedSongController.execute(state.getSpotifyUser(), state.getGroupUsers());
        });

        groupAnalytics.addActionListener(evt -> {
            if (!evt.getSource().equals(groupAnalytics)) {
                return;
            }

            UserGroupState state = inGroupViewModel.getState();

            if (state.getGroupUsers() == null || state.getGroupUsers().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No group members found to analyze.",
                        "Group Analytics",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (groupAnalyticsController == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Group analytics controller is not wired.",
                        "Internal Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                SpotifyDataAccessObject spotifyDAO = new SpotifyDataAccessObject();
                List<UserTasteProfile> profiles = new ArrayList<>();

                for (SpotifyUser u : state.getGroupUsers()) {
                    if (u == null || u.getAccessToken() == null) {
                        // Skip placeholder users with no tokens
                        continue;
                    }
                    Set<String> genres = spotifyDAO.getUserTopGenres(u);

                    profiles.add(new UserTasteProfile(
                            u.getUsername(),
                            u.getSpotifyUserId(),
                            genres
                    ));
                }

                if (profiles.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Couldn't build taste profiles for any group members.",
                            "Group Analytics",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                //Call the use case
                groupAnalyticsController.analyzeProfiles(profiles);

                // Switch to the group analytics view (the presenter updates the VM)
                viewManagerModel.setState(groupAnalyticsViewModel.getViewName());
                viewManagerModel.firePropertyChange();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to analyze group: " + e.getMessage(),
                        "Group Analytics Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        this.setLayout(new BorderLayout());
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.NORTH);

    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("shared song error")) {
            JOptionPane.showMessageDialog(this,
                    sharedSongViewModel.getState().getErrorMessage(),
                    "Shared Song Error",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            final UserGroupState state = (UserGroupState) evt.getNewValue();
            setGroupName(state.getGroupName());
            setGroupPanel(state);
        }
    }

    public void setSharedSongController(SharedSongController sharedSongController) {
        this.sharedSongController = sharedSongController;
    }

    public void setGroupAnalyticsController(GroupAnalyticsController groupAnalyticsController) {
        this.groupAnalyticsController = groupAnalyticsController;
    }

    public void setGroupName(String groupName) {
        this.groupName.setText("Group Name: " + groupName.trim());
    }

    public void setGroupPanel(UserGroupState state) {
        // default logic to do nothing if group state is nothing
        if (state.getGroupUsernames() == null  || state.getGroupUsernames().isEmpty()) { return; }

        groupPanel.removeAll();
        groupPanel.add(Box.createVerticalStrut(7));

        if (state.getGroupUsernames() != null) {
            groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
            for (String username : state.getGroupUsernames()) {
                JLabel usernameLabel = new JLabel(username);
                usernameLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                usernameLabel.setFont(new Font("Century Schoolbook", Font.PLAIN, 18));
                groupPanel.add(usernameLabel);
            }
        }

        groupPanel.revalidate();
        groupPanel.repaint();
    }
    
    public String getViewName() { return viewName; }
}