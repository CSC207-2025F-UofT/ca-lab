package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Signup Use Case.
 */
public class SignupView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "sign up";

    private final SignupViewModel signupViewModel;
    private final JTextField firstnameInputField = new JTextField(15);
    private final JTextField lastnameInputField = new JTextField(15);
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private SignupController signupController = null;

    private final JButton toLogin;

    private final JButton visitorSignupButton = new JButton("Signup as Visitor");
    private final JButton adminSignupButton = new JButton("Signup as Admin");
    private final JTextField adminKeyField = new JTextField(15);

    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        signupViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel firstnameInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.FIRSTNAME_LABEL), firstnameInputField);
        final LabelTextPanel lastnameInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.LASTNAME_LABEL), lastnameInputField);
        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.USERNAME_LABEL), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.PASSWORD_LABEL), passwordInputField);
        final LabelTextPanel repeatPasswordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.REPEAT_PASSWORD_LABEL), repeatPasswordInputField);


        final JPanel buttons = new JPanel();
        toLogin = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        buttons.add(toLogin);

        toLogin.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        signupController.switchToLoginView();
                    }
                }
        );


        addFirstnameListener();
        addLastnameListener();
        addUsernameListener();
        addPasswordListener();
        addRepeatPasswordListener();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(firstnameInfo);
        this.add(lastnameInfo);
        this.add(usernameInfo);
        this.add(passwordInfo);
        this.add(repeatPasswordInfo);
        final JPanel typeButtons = new JPanel();
        typeButtons.add(visitorSignupButton);
        typeButtons.add(adminSignupButton);
        this.add(typeButtons);

        // ===== Visitor Signup Button =====
        visitorSignupButton.addActionListener(e -> {
            SignupState currentState = signupViewModel.getState();
            currentState.setUserType("visitor");
            signupViewModel.setState(currentState);
            signupController.execute(
                    currentState.getUserType(),
                    currentState.getFirstname(),
                    currentState.getLastname(),
                    currentState.getUsername(),
                    currentState.getPassword(),
                    currentState.getRepeatPassword()
            );
        });

        // ===== Admin Signup Button =====
        adminSignupButton.addActionListener(e -> {
            String enteredKey = JOptionPane.showInputDialog(this, "Enter Admin Key:");
            if (enteredKey != null && enteredKey.equals("admin123")) {
                SignupState currentState = signupViewModel.getState();
                currentState.setUserType("administrator");
                signupViewModel.setState(currentState);
                signupController.execute(
                        currentState.getUserType(),
                        currentState.getFirstname(),
                        currentState.getLastname(),
                        currentState.getUsername(),
                        currentState.getPassword(),
                        currentState.getRepeatPassword()
                );
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin key! Please try again or sign up as a visitor.");
            }
        });
        this.add(buttons);
    }

    private void addFirstnameListener() {
        firstnameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setFirstname(firstnameInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addLastnameListener() {
        lastnameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setLastname(lastnameInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addUsernameListener() {
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                signupViewModel.setState(currentState);
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
    }

    private void addPasswordListener() {
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                signupViewModel.setState(currentState);
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
    }

    private void addRepeatPasswordListener() {
        repeatPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
                signupViewModel.setState(currentState);
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
    }

    @Override
    public void actionPerformed(ActionEvent evt) {}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SignupState state = (SignupState) evt.getNewValue();
        if (state.getUsernameError() != null) {
            JOptionPane.showMessageDialog(this, state.getUsernameError());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setSignupController(SignupController controller) {
        this.signupController = controller;
    }
}
