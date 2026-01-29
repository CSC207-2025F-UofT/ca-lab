package view;

import data_access.GroupDataAccessObject;
import data_access.SpotifyUserDataAccessObject;
import entity.SpotifyUser;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
import interface_adapter.create_group.CreateGroupController;
import interface_adapter.create_group.InGroupViewModel;
import interface_adapter.create_group.NoGroupViewModel;
import interface_adapter.create_group.UserGroupState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoGroupView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "no group";
    private final NoGroupViewModel noGroupViewModel;
    private final InGroupViewModel inGroupViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInView loggedInView;

    private final JTextField groupNameField = new JTextField();
    private final JLabel nameErrorField = new JLabel();
    private final JTextField membersField = new JTextField();

    private final JButton loggedIn;
    private final JButton createGroup;
    private CreateGroupController createGroupController = null;

    public NoGroupView(NoGroupViewModel noGroupViewModel,
                       InGroupViewModel inGroupViewModel,
                       ViewManagerModel viewManagerModel,
                       LoggedInView loggedInView) {
        this.noGroupViewModel = noGroupViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loggedInView = loggedInView;
        this.inGroupViewModel = inGroupViewModel;
        this.noGroupViewModel.addPropertyChangeListener(this);


        JPanel topPanel = new JPanel();
        loggedIn = new JButton("Main Page");
        loggedIn.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(loggedIn);

        final JLabel title = new JLabel("You're not in a group :(");
        title.setFont(new Font("Century Schoolbook", Font.PLAIN, 20));
        title.setAlignmentX(CENTER_ALIGNMENT);
        topPanel.add(title);

        final JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));

        fields.add(Box.createVerticalStrut(10));
        groupNameField.setColumns(30);
        JLabel groupNameLabel = new JLabel("Group Name:");
        groupNameLabel.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));
        final LabelTextPanel enterGroupName = new LabelTextPanel(groupNameLabel, groupNameField);
        fields.add(enterGroupName);

        membersField.setColumns(30);
        JLabel membersLabel = new JLabel("Member Spotify User IDs");
        membersLabel.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));
        final LabelTextPanel membersPanel = new LabelTextPanel(membersLabel, membersField);
        fields.add(membersPanel);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        createGroup = new JButton("Create Group");
        createGroup.setAlignmentY(CENTER_ALIGNMENT);
        createGroup.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));
        buttons.add(createGroup);

        fields.add(Box.createVerticalStrut(2));
        fields.add(buttons);

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

        createGroup.addActionListener(e -> {
                    String groupName = groupNameField.getText().trim();
                    String membersText = membersField.getText().trim();

                    if (groupName.isEmpty()) {
                        JOptionPane.showMessageDialog(this.getParent(),
                                "Please enter a group name.",
                                "Missing Name",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
//                    Split IDs
            String[] memberIds = membersText.isEmpty() ? new String[0] :
                    Arrays.stream(membersText.split("\\s*,\\s*"))
                            .map(String::trim)
                            .toArray(String[]::new);

            SpotifyUserDataAccessObject spotifyUserDao = SpotifyUserDataAccessObject.getInstance();
            java.util.List<SpotifyUser> members = new ArrayList<>();
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
                JOptionPane.showMessageDialog(this.getParent(),
                        "No valid users found. Please enter at least one valid Spotify User ID.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!missingIds.isEmpty()) {
                JOptionPane.showMessageDialog(this.getParent(),
                        "These user IDs were not found: " + String.join(", ", missingIds) + "\nThey will be skipped.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

            if (createGroupController != null) {
                createGroupController.execute(groupName, loggedInView.getCurrentSpotifyUser(), members);

                // mark logged-in user as in a group
                loggedInView.getCurrentSpotifyUser().setInGroup(true);

                JOptionPane.showMessageDialog(this.getParent(),
                        "Group created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(this.getParent(),
                        "Error creating group. Make sure the controller is initialized.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(fields, BorderLayout.CENTER);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("createError")) {
            setNameError(noGroupViewModel.getState().getNameError());
        }
    }

    public String getViewName() { return viewName; }

    public void setCreateGroupController(CreateGroupController createGroupController) {
        this.createGroupController = createGroupController;
    }

    public void setNameError(String nameError) { this.nameErrorField.setText(nameError); }
}
