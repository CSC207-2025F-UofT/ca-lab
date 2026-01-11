package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.CreateGroupController;
import interface_adapter.create_group.NoGroupViewModel;
import interface_adapter.create_group.UserGroupState;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NoGroupView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "no group";
    private final NoGroupViewModel noGroupViewModel;
    private final ViewManagerModel viewManagerModel;

    private final JTextField inputGroupNameField = new JTextField();
    private final JLabel nameErrorField = new JLabel();

    private final JButton loggedIn;
    private final JButton createGroup;
    private final JButton joinGroup;
    private CreateGroupController createGroupController = null;
//    private JoinGroupController joinGroupController = null;

    public NoGroupView(NoGroupViewModel noGroupViewModel, ViewManagerModel viewManagerModel) {
        this.noGroupViewModel = noGroupViewModel;
        this.viewManagerModel = viewManagerModel;
        this.noGroupViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("You're not in a group");
        title.setAlignmentX(CENTER_ALIGNMENT);

        final LabelTextPanel enterGroupName = new LabelTextPanel(
                new JLabel("Group Name"), inputGroupNameField);

        loggedIn = new JButton("Main Page");
        final JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        createGroup = new JButton("Create Group");
        buttons.add(createGroup);
        joinGroup = new JButton("Join Group");
        buttons.add(joinGroup);

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

        createGroup.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(createGroup)) {
                            if (createGroupController == null) {
                                JOptionPane.showMessageDialog(NoGroupView.this,
                                        "Group creation is not wired up yet.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            String name = inputGroupNameField.getText().trim();
                            if (name.isEmpty()) {
                                JOptionPane.showMessageDialog(NoGroupView.this,
                                        "Please enter a group name.",
                                        "Invalid Group Name",
                                        JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            // For now, we don't have members here; pass null/empty list
                            final UserGroupState currentState = noGroupViewModel.getState();

                            try {
                                createGroupController.execute(
                                        name,
                                        currentState.getSpotifyUser(), // may be null; that's OK if controller handles it
                                        currentState.getGroupUsers()   // may be null; controller will treat as empty
                                );
                            } catch (IllegalArgumentException e) {
                                JOptionPane.showMessageDialog(NoGroupView.this,
                                        e.getMessage(),
                                        "Create Group Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
        );

//        joinGroup.addActionListener(
//                new ActionListener() {
//                    public void actionPerformed(ActionEvent evt) {
//                        if (evt.getSource().equals(joinGroup)) {
//                            final UserGroupState = noGroupViewModel.getState();
//                            joinGroupController.execute();
//                            TODO
//                        }
//                    }
//                }
//        );

        inputGroupNameField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final UserGroupState currentState = noGroupViewModel.getState();
                currentState.setGroupName(inputGroupNameField.getText());
                noGroupViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.fill = GridBagConstraints.HORIZONTAL;
        gbcTitle.anchor = GridBagConstraints.CENTER;
        gbcTitle.gridx = 1;
        gbcTitle.gridy = 1;
        gbcTitle.weighty = 0.2;
        this.add(title, gbcTitle);

        GridBagConstraints gbcGroup = new GridBagConstraints();
        gbcGroup.gridheight = 4;
        gbcGroup.gridwidth = 3;
        gbcGroup.anchor = GridBagConstraints.LINE_START;
        gbcGroup.fill = GridBagConstraints.HORIZONTAL;
        gbcGroup.gridx = 0;
        gbcGroup.gridy = 2;
        gbcGroup.gridwidth = 2;
        gbcGroup.weighty = 0.3;
        this.add(enterGroupName, gbcGroup);

        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.fill = GridBagConstraints.VERTICAL;
        gbcButtons.anchor = GridBagConstraints.CENTER;
        gbcButtons.gridx = 2;
        gbcButtons.gridy = 2;
        gbcButtons.weighty = 0.5;
        this.add(buttons, gbcButtons);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        this.add(loggedIn, c);
    }
    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    public void propertyChange(PropertyChangeEvent evt) {
//        if (evt.getPropertyName().equals("creatError")) {}
        setNameError(noGroupViewModel.getState().getNameError());
    }

    public String getViewName() { return viewName; }

    public void setCreateGroupController(CreateGroupController createGroupController) {
        this.createGroupController = createGroupController;
    }
//    public void setJoinGroupController(JoinGorupController joinGroupController) {
//        this.joinGroupController = joinGroupController
//    }
    public void setNameError(String nameError) { this.nameErrorField.setText("nameError"); }
}
