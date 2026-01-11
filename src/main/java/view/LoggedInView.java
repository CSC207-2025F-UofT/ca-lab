package view;

import data_access.SpotifyDataAccessObject;
import data_access.SpotifyUserDataAccessObject;
import entity.SpotifyUser;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.ViewManagerModel;
import interface_adapter.loyalty_score.LoyaltyController;
import interface_adapter.spotify_auth.SpotifyAuthViewModel;
import interface_adapter.daily_mix.DailyMixViewModel;
import interface_adapter.daily_mix.DailyMixState;
import interface_adapter.daily_mix.DailyMixController;
import interface_adapter.get_topItems.GetTopItemsViewModel;
import interface_adapter.get_topItems.GetTopItemsController;
import interface_adapter.get_topItems.GetTopItemsState;
import use_case.get_topItems.TimeRange;
import use_case.get_topItems.TopItem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {
    private final JButton groupTabButton;
    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SpotifyAuthViewModel spotifyAuthViewModel;

    private LogoutController logoutController;
    private SpotifyDataAccessObject spotifyDAO;
    private SpotifyUser currentSpotifyUser;

    private final JLabel spotifyStatusLabel = new JLabel();

    private final JButton logOut;
    private final JButton connectSpotifyButton;

    private final DailyMixViewModel dailyMixViewModel;
    private DailyMixController dailyMixController;
    private final JButton generateDailyMixButton;
    private interface_adapter.create_group.CreateGroupController createGroupController;
    private final GetTopItemsViewModel getTopItemsViewModel;
    private GetTopItemsController getTopItemsController;
    private LoyaltyController loyaltyController;
    private final JButton getTopItemButton;
    private final JButton artistsButton;
    private final JButton tracksButton;
    private final JButton short_termButton;
    private final JButton medium_termButton;
    private final JButton long_termButton;
    private final JTextArea TopItemsArea;
    private final JButton goToGroup;
    //    private GroupDataAccessObject groupDAO;

    public LoggedInView(LoggedInViewModel loggedInViewModel,
                        ViewManagerModel viewManagerModel,
                        SpotifyAuthViewModel spotifyAuthViewModel,
                        DailyMixViewModel dailyMixViewModel,
                        GetTopItemsViewModel getTopItemsViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
        this.spotifyAuthViewModel = spotifyAuthViewModel;
        this.dailyMixViewModel = dailyMixViewModel;
        this.getTopItemsViewModel = getTopItemsViewModel;

        this.loggedInViewModel.addPropertyChangeListener(this);
        this.dailyMixViewModel.addPropertyChangeListener(this);
        this.getTopItemsViewModel.addPropertyChangeListener(this);
        this.spotifyDAO = new SpotifyDataAccessObject();


        final JLabel title = new JLabel("Better Wrapped - Dashboard");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Spotify status panel
        final JPanel spotifyPanel = new JPanel();
        spotifyPanel.add(new JLabel("Spotify Status: "));
        spotifyStatusLabel.setText("Not Connected");
        spotifyStatusLabel.setForeground(Color.RED);
        spotifyPanel.add(spotifyStatusLabel);

        // Buttons panel
        final JPanel buttons = new JPanel();
        logOut = new JButton("Log Out");
        buttons.add(logOut);

        connectSpotifyButton = new JButton("Connect Spotify");
        buttons.add(connectSpotifyButton);

        groupTabButton = new JButton("Create Group");

        // Daily Mix button
        generateDailyMixButton = new JButton("Generate Daily Mix");
        generateDailyMixButton.setEnabled(false);
        buttons.add(generateDailyMixButton);

        // TopItems text
        TopItemsArea = new JTextArea(10, 30);
        TopItemsArea.setEditable(false);
        TopItemsArea.setFont(new Font("Monospaced", Font.PLAIN, 21));
        JScrollPane TopItemsScroll = new JScrollPane(TopItemsArea);

        // get TopItems buttons
        getTopItemButton = new JButton("<html>Get Top<br>Artist/Track</html>");
        getTopItemButton.setEnabled(false); // disabled until time and item selected
        artistsButton =  new JButton("Top Artists");
        artistsButton.setEnabled(false);
        tracksButton = new JButton("Top Tracks");
        tracksButton.setEnabled(false);
        short_termButton = new JButton("1 month");
        short_termButton.setEnabled(false);
        medium_termButton = new JButton("6 months");
        medium_termButton.setEnabled(false);
        long_termButton = new JButton("1 year");
        long_termButton.setEnabled(false);


        // Log out button listener
        logOut.addActionListener(evt -> {
            if (logoutController != null) {
                logoutController.execute();
            }
        });

        // Connect Spotify listener
        connectSpotifyButton.addActionListener(evt -> {
            viewManagerModel.setState(spotifyAuthViewModel.getViewName());
            viewManagerModel.firePropertyChange();
        });

        // Daily Mix listener
        generateDailyMixButton.addActionListener(evt -> {
            if (dailyMixController == null || currentSpotifyUser == null) {
                JOptionPane.showMessageDialog(this,
                        "Please connect to your Spotify account first.",
                        "No Spotify User",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                dailyMixController.execute(this.getCurrentSpotifyUser(), 20);
            }
        });

        // Generate time button listeners
        short_termButton.addActionListener(evt -> {
            if (evt.getSource().equals(short_termButton)) {
                if (getTopItemsController == null || currentSpotifyUser == null) {
                    JOptionPane.showMessageDialog(this,
                            "Please connect your Spotify account first.",
                            "No Spotify User",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    set_time_shortTerm();
                    checkEnableGetTopItemButton();
                }
            }
        });

        //Group tab button listeners

        groupTabButton.addActionListener(evt -> {
            // Go straight to the create group popup
            openCreateGroupPopup();
        });


        medium_termButton.addActionListener(evt -> {
            if (evt.getSource().equals(medium_termButton)) {
                if (getTopItemsController == null || currentSpotifyUser == null) {
                    JOptionPane.showMessageDialog(this,
                            "Please connect your Spotify account first.",
                            "No Spotify User",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    set_time_mediumTerm();
                    checkEnableGetTopItemButton();
                }
            }
        });

        long_termButton.addActionListener(evt -> {
            if (evt.getSource().equals(long_termButton)) {
                if (getTopItemsController == null || currentSpotifyUser == null) {
                    JOptionPane.showMessageDialog(this,
                            "Please connect your Spotify account first.",
                            "No Spotify User",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    set_time_longTerm();
                    checkEnableGetTopItemButton();
                }
            }
        });

        // Generate getTopItems button listener
        getTopItemButton.addActionListener(evt -> {
            if (evt.getSource().equals(getTopItemButton)) {
                if (getTopItemsController == null || currentSpotifyUser == null) {
                    JOptionPane.showMessageDialog(this,
                            "Please select time range and top item first.",
                            "unselected item or time",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                final GetTopItemsState getTopItemsState = getTopItemsViewModel.getState();
                TopItem topItem = getTopItemsState.getSelectedTopItem();
                TimeRange timeRange = getTopItemsState.getSelectedTime();
                getTopItemsController.execute(currentSpotifyUser, topItem, timeRange);

            }
        });

        // Time range listeners
        short_termButton.addActionListener(evt -> { set_time_shortTerm();});
        medium_termButton.addActionListener(evt -> { set_time_mediumTerm();});
        long_termButton.addActionListener(evt -> { set_time_longTerm();});

        // TopItem listeners
        artistsButton.addActionListener(evt -> { set_item_artist();});
        tracksButton.addActionListener(evt -> { set_item_tracks();});


        // Main layout
        this.setLayout(new BorderLayout());

        // --- Left panel (WEST) ---
        // Use BoxLayout to allow vertical centering
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(Box.createVerticalGlue());   // pushes buttons to vertical center
        leftPanel.add(getTopItemButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(artistsButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(tracksButton);
        leftPanel.add(Box.createVerticalGlue());
        this.add(leftPanel, BorderLayout.WEST);

        // -- TOP panel --

        // If you want to add a button panel for group stuff, I recommend adding it to this
        // Make another separate panel for the buttons, and add it to the topWrapper as defined below

        // Time range buttons
        JPanel timePanel = new JPanel();
        timePanel.add(short_termButton);
        timePanel.add(medium_termButton);
        timePanel.add(long_termButton);

        // --- Lookup bar panel ---
        JPanel loyaltyLookUpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel loyaltyLookUpLabel = new JLabel("Enter an artist's name to find their loyalty score:");
        loyaltyLookUpLabel.setFont(new Font("Calibri", Font.PLAIN, 18));

        JTextField lookupField = new JTextField(20);
        lookupField.setFont(new Font("Monospaced", Font.PLAIN, 18));

        // Look up artist score, when user hits enter
        lookupField.addActionListener(e -> {
            String artistName = lookupField.getText().trim();
            if (loyaltyController == null ||  currentSpotifyUser == null) {
                System.out.print(currentSpotifyUser.toString() + loyaltyController.toString());
                JOptionPane.showMessageDialog(this,
                        "Please connect to your Spotify account first.",
                        "No Spotify User",
                        JOptionPane.WARNING_MESSAGE);
            }
                else { loyaltyController.execute(this.getCurrentSpotifyUser(), artistName); }
        });

        loyaltyLookUpPanel.add(loyaltyLookUpLabel);
        loyaltyLookUpPanel.add(lookupField);

        // Go to Group View Button
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        goToGroup = new JButton("Group");
        goToGroup.addActionListener(e -> {
            if (currentSpotifyUser != null && currentSpotifyUser.isInGroup()) {
                viewManagerModel.setState("in group");
                viewManagerModel.firePropertyChange();
            } else {
                viewManagerModel.setState("no group");
                viewManagerModel.firePropertyChange();
            }
        });

        goToGroup.setEnabled(false);
        groupPanel.add(goToGroup);

        // --- Center panel (CENTER) ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
        topWrapper.add(title);
        topWrapper.add(spotifyPanel);
        topWrapper.add(groupPanel);
        topWrapper.add(timePanel);
        loyaltyLookUpPanel.add(loyaltyLookUpLabel);
        loyaltyLookUpPanel.add(lookupField);

        // Add lookup bar to the same top wrapper, right under the time buttons
        topWrapper.add(Box.createVerticalStrut(10)); // optional spacing
        topWrapper.add(loyaltyLookUpPanel);

        centerPanel.add(topWrapper, BorderLayout.NORTH);
        centerPanel.add(TopItemsScroll, BorderLayout.CENTER);

        this.add(centerPanel, BorderLayout.CENTER);
        JPanel groupButtonsPanel = new JPanel();
        groupButtonsPanel.add(groupTabButton);
        topWrapper.add(Box.createVerticalStrut(10)); // spacing
        topWrapper.add(groupButtonsPanel);

        topWrapper.add(Box.createVerticalStrut(10)); // spacing
        topWrapper.add(groupButtonsPanel);

        // --- Bottom panel (SOUTH) ---
        JPanel bottomPanel = new JPanel();

        bottomPanel.add(logOut);
        bottomPanel.add(connectSpotifyButton);
        bottomPanel.add(generateDailyMixButton);
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 100));
        this.add(bottomPanel, BorderLayout.SOUTH);

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();

        // --------------------------
        // LoggedInState update
        // --------------------------
        if (newValue instanceof LoggedInState) {
            LoggedInState state = (LoggedInState) newValue;

            if (evt.getPropertyName().equals("state")) {
                // Spotify status
                boolean connected = state.isSpotifyAuthenticated();
                spotifyStatusLabel.setText(connected ? "Connected ✓" : "Not Connected");
                spotifyStatusLabel.setForeground(connected ? Color.GREEN : Color.RED);
                connectSpotifyButton.setEnabled(!connected);

                generateDailyMixButton.setEnabled(connected);
                tracksButton.setEnabled(connected);
                artistsButton.setEnabled(connected);
                short_termButton.setEnabled(connected);
                medium_termButton.setEnabled(connected);
                long_termButton.setEnabled(connected);
                goToGroup.setEnabled(connected);
            }
        }


        // --------------------------
        // GetTopItemsState update
        // --------------------------
        else if (newValue instanceof DailyMixState) {
            DailyMixState mixState = (DailyMixState) newValue;

            if (mixState.getError() != null) {
                JOptionPane.showMessageDialog(this,
                        mixState.getError(),
                        "Daily Mix Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Show in new window:
            showDailyMixInNewWindow(mixState.getTracks());
        }

        else if (newValue instanceof GetTopItemsState) {
            GetTopItemsState getTopItemsState = (GetTopItemsState) newValue;
            StringBuilder sb = new StringBuilder();

            if (getTopItemsState.getSelectedTime() == null ){
                sb.append("please select a time range!");
            }

            else if (getTopItemsState.getSelectedTopItem() == null ){
                sb.append("please select either artist or track! ");
            }

            else if (getTopItemsState.getTopItems() == null || getTopItemsState.getTopItems().isEmpty()) {
                sb.append("no items found. Try listening to some music on Spotify!");

            } else {
                int index = 1;
                for (String item : getTopItemsState.getTopItems()) {
                    sb.append(index).append(". ").append(item).append("\n \n");
                    index++;
                }
            }

            TopItemsArea.setText(sb.toString());
            TopItemsArea.setCaretPosition(0);
        }

    }

    // group helpers
    private void openCreateGroupPopup() {
        // Popup frame
        JFrame createGroupFrame = new JFrame("Create Group");
        createGroupFrame.setLayout(new BoxLayout(createGroupFrame.getContentPane(), BoxLayout.Y_AXIS));
        createGroupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Group Name
        JPanel groupNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel groupNameLabel = new JLabel("Group Name:");
        JTextField groupNameField = new JTextField(20);
        groupNamePanel.add(groupNameLabel);
        groupNamePanel.add(groupNameField);

        // Member Spotify User IDs
        JPanel membersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel membersLabel = new JLabel("Member Spotify User IDs (comma-separated):");
        JTextField membersField = new JTextField(30);
        membersPanel.add(membersLabel);
        membersPanel.add(membersField);

        // Create button
        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            String groupName = groupNameField.getText().trim();
            String membersText = membersField.getText().trim();

            if (groupName.isEmpty()) {
                JOptionPane.showMessageDialog(createGroupFrame,
                        "Please enter a group name.",
                        "Missing Name",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Split IDs
            String[] memberIds = membersText.isEmpty() ? new String[0] :
                    Arrays.stream(membersText.split("\\s*,\\s*"))
                            .map(String::trim)
                            .toArray(String[]::new);

            SpotifyUserDataAccessObject spotifyUserDao = SpotifyUserDataAccessObject.getInstance();
            List<SpotifyUser> members = new ArrayList<>();
            List<String> missingIds = new ArrayList<>();

            for (String id : memberIds) {
                SpotifyUser user = spotifyUserDao.getUserById(id);
                if (user != null) {
                    members.add(user);
                } else {
                    missingIds.add(id);
                }
            }

            if (members.isEmpty()) {
                JOptionPane.showMessageDialog(createGroupFrame,
                        "No valid users found. Please enter at least one valid Spotify User ID.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!missingIds.isEmpty()) {
                JOptionPane.showMessageDialog(createGroupFrame,
                        "These user IDs were not found: " + String.join(", ", missingIds) + "\nThey will be skipped.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

            if (createGroupController != null) {
                createGroupController.execute(groupName, currentSpotifyUser, members);

                // mark logged-in user as in a group
                currentSpotifyUser.setInGroup(true);

                JOptionPane.showMessageDialog(createGroupFrame,
                        "Group created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                createGroupFrame.dispose();
            }
            else {
                JOptionPane.showMessageDialog(createGroupFrame,
                        "Error creating group. Make sure the controller is initialized.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        createGroupFrame.add(groupNamePanel);
        createGroupFrame.add(membersPanel);
        createGroupFrame.add(createButton);
        createGroupFrame.pack();
        createGroupFrame.setVisible(true);
    }

    // --- Time and Item helpers ---
    private void set_time_shortTerm() {
        final GetTopItemsState state = getTopItemsViewModel.getState();
        state.setSelectedTime(TimeRange.short_term);
        getTopItemsViewModel.setState(state);
        getTopItemsViewModel.firePropertyChange();
    }

    private void set_time_mediumTerm() {
        final GetTopItemsState state = getTopItemsViewModel.getState();
        state.setSelectedTime(TimeRange.medium_term);
        getTopItemsViewModel.setState(state);
        getTopItemsViewModel.firePropertyChange();
    }

    private void set_time_longTerm() {
        final GetTopItemsState state = getTopItemsViewModel.getState();
        state.setSelectedTime(TimeRange.long_term);
        getTopItemsViewModel.setState(state);
        getTopItemsViewModel.firePropertyChange();
    }

    private void set_item_tracks() {
        final GetTopItemsState state = getTopItemsViewModel.getState();
        state.setSelectedTopItem(TopItem.tracks);
        getTopItemsViewModel.setState(state);
        getTopItemsViewModel.firePropertyChange();
    }

    private void set_item_artist() {
        final GetTopItemsState state = getTopItemsViewModel.getState();
        state.setSelectedTopItem(TopItem.artists);
        getTopItemsViewModel.setState(state);
        getTopItemsViewModel.firePropertyChange();
    }

    // --- Controllers and user setter ---
    public void setCreateGroupController(interface_adapter.create_group.CreateGroupController controller) {
        this.createGroupController = controller;
    }

    public void setDailyMixController(DailyMixController dailyMixController) {
        this.dailyMixController = dailyMixController;
    }

    public void setGetTopItemsController(GetTopItemsController getTopItemsController) {
        this.getTopItemsController = getTopItemsController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public void setLoyaltyLookupController(LoyaltyController loyaltyController) {
        this.loyaltyController = loyaltyController;
    }

    public void setCurrentSpotifyUser(SpotifyUser user) {
        this.currentSpotifyUser = user;
    }

    public SpotifyUser getCurrentSpotifyUser() { return this.currentSpotifyUser; }

    public String getViewName() {
        return viewName;
    }

    private void checkEnableGetTopItemButton(){
        final GetTopItemsState getTopItemsState = getTopItemsViewModel.getState();
        boolean enabled = getTopItemsState.getSelectedTopItem() != null
                && getTopItemsState.getSelectedTime() != null;
        getTopItemButton.setEnabled(enabled);
    }

    // private method for showing daily mix instead of on same view

    private void showDailyMixInNewWindow(List<String> tracks) {
        JFrame frame = new JFrame("Your Daily Mix");
        JTextArea textArea = new JTextArea(15, 40);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        if (tracks == null || tracks.isEmpty()) {
            sb.append("No tracks found. Try saving some songs or playing music on Spotify!");
        } else {
            int index = 1;
            for (String track : tracks) {
                sb.append(index).append(". ").append(track).append("\n");
                index++;
            }
        }
        textArea.setText(sb.toString());
        textArea.setCaretPosition(0);

        frame.add(new JScrollPane(textArea));
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }
}

