package view;

import interface_adapter.signup.SignupState;
import interface_adapter.submit_application.SubmitController;
import interface_adapter.submit_application.SubmitState;
import interface_adapter.submit_application.SubmitViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SubmitView extends JPanel implements ActionListener, PropertyChangeListener {
    final String viewName = "Submit";
    private final SubmitViewModel submitViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JTextField firstnameInputField = new JTextField(15);
    private final JTextField lastnameInputField = new JTextField(15);
    private final JTextField ageInputField = new JTextField(3);
    private final JTextField occupationInputField = new JTextField(15);
    private final JTextField addressInputField = new JTextField(100);
    private final JTextField homeEviInputField = new JTextField(200);
    private final JTextField telInputField = new JTextField(20);
    private final JTextField emailInputField = new JTextField(30);
    private final JTextField reasonInputField = new JTextField(200);
    private final JTextField prevExpInputField = new JTextField(100);
    private final JTextField availabilityInputField = new JTextField(100);
    private SubmitController submitController = null;

    private final JButton submit;
    private final JButton cancel;

    public SubmitView(SubmitViewModel submitViewModel) {
        this.submitViewModel = submitViewModel;
        submitViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(SubmitViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel(SubmitViewModel.USERNAME_LABEL), usernameInputField);
        final LabelTextPanel firstnameInfo = new LabelTextPanel(
                new JLabel(SubmitViewModel.FIRSTNAME_LABEL), firstnameInputField);
        final LabelTextPanel lastnameInfo = new LabelTextPanel(
                new JLabel(SubmitViewModel.LASTNAME_LABEL), lastnameInputField);
        final LabelTextPanel ageInfo = new LabelTextPanel(
                new JLabel(SubmitViewModel.AGE_LABEL), ageInputField);
        final LabelTextPanel occupationInfo = new LabelTextPanel(
                new JLabel(SubmitViewModel.OCCUPATION_LABEL), occupationInputField);
        final LabelTextPanel addressInfo = new LabelTextPanel(
                new JLabel(SubmitViewModel.ADDRESS_LABEL), addressInputField);
        final LabelTextPanel telInfo = new LabelTextPanel(
                new JLabel(SubmitViewModel.TEL_LABEL), telInputField);
        final LabelTextPanel emailInfo = new LabelTextPanel(
                new JLabel(SubmitViewModel.EMAIL_LABEL), emailInputField);

        final JPanel homeEviInfo = new JPanel();
        homeEviInfo.setLayout(new BoxLayout(homeEviInfo, BoxLayout.Y_AXIS));
        homeEviInfo.add(new JLabel(SubmitViewModel.HOME_LABEL));
        homeEviInfo.add(homeEviInputField);

        final JPanel reasonInfo = new JPanel();
        reasonInfo.setLayout(new BoxLayout(reasonInfo, BoxLayout.Y_AXIS));
        reasonInfo.add(new JLabel(SubmitViewModel.REASON_LABEL));
        reasonInfo.add(reasonInputField);

        final JPanel prevExpInfo = new JPanel();
        prevExpInfo.setLayout(new BoxLayout(prevExpInfo, BoxLayout.Y_AXIS));
        prevExpInfo.add(new JLabel(SubmitViewModel.EXP_LABEL));
        prevExpInfo.add(prevExpInputField);

        final JPanel availabilityInfo = new JPanel();
        availabilityInfo.setLayout(new BoxLayout(availabilityInfo, BoxLayout.Y_AXIS));
        prevExpInfo.add(new JLabel(SubmitViewModel.AVAILABILITY_LABEL));
        prevExpInfo.add(availabilityInputField);

        final JPanel buttons = new JPanel();
        submit = new JButton(SubmitViewModel.SUBMIT_BUTTON_LABEL);
        buttons.add(submit);
        cancel = new JButton(SubmitViewModel.CANCEL_BUTTON_LABEL);
        buttons.add(cancel);

        submit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(submit)) {
                            final SubmitState currentState = submitViewModel.getState();

                            submitController.execute(
                                    currentState.getPetId(),
                                    currentState.getUsername(),
                                    currentState.getFirstname(),
                                    currentState.getLastname(),
                                    currentState.getAge(),
                                    currentState.getOccupation(),
                                    currentState.getAddress(),
                                    currentState.getHomeEvi(),
                                    currentState.getTel(),
                                    currentState.getEmail(),
                                    currentState.getReason(),
                                    currentState.getPrevExp(),
                                    currentState.getAvailability()
                            );

                            JOptionPane.showMessageDialog(SubmitView.this,
                                    "Your application has been successfully submitted!");

                        }
                    }
                }
        );

        cancel.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        submitController.switchToBrowserFilterView();
                    }
                });

        addUsernameListener();
        addFirstnameListener();
        addLastnameListener();
        addAgeListener();
        addOccupationListener();
        addAddressListener();
        addHomeListener();
        addTelListener();
        addEmailListener();
        addReasonListener();
        addExpListener();
        addAvailabilityListener();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(usernameInfo);
        this.add(firstnameInfo);
        this.add(lastnameInfo);
        this.add(ageInfo);
        this.add(occupationInfo);
        this.add(addressInfo);
        this.add(telInfo);
        this.add(emailInfo);
        this.add(reasonInfo);
        this.add(prevExpInfo);
        this.add(availabilityInfo);
        this.add(buttons);
    }

    private void addUsernameListener() {
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addFirstnameListener() {
        firstnameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setFirstname(firstnameInputField.getText());
                submitViewModel.setState(currentState);
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
                final SubmitState currentState = submitViewModel.getState();
                currentState.setLastname(lastnameInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addAgeListener() {
        ageInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setAge(ageInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addOccupationListener() {
        occupationInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setOccupation(occupationInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addAddressListener() {
        addressInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setAddress(addressInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addHomeListener() {
        homeEviInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setHomeEvi(homeEviInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addTelListener() {
        telInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setTel(telInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addEmailListener() {
        emailInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setEmail(emailInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addReasonListener() {
        reasonInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setReason(reasonInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addExpListener() {
        prevExpInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setPrevExp(prevExpInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }

    private void addAvailabilityListener() {
        availabilityInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setAvailability(availabilityInputField.getText());
                submitViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void removeUpdate(DocumentEvent e) {documentListenerHelper();}

            @Override
            public void changedUpdate(DocumentEvent e) {documentListenerHelper();}
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SubmitState state = (SubmitState) evt.getNewValue();
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError());
        }
        if (state.getSuccess() != null) {
            JOptionPane.showMessageDialog(this, state.getSuccess());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setSubmitController(SubmitController controller) {
        this.submitController = controller;
    }
}
