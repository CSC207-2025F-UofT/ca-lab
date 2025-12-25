package view;

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
    final String viewName = "submit";
    private final SubmitViewModel submitViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JTextField firstnameInputField = new JTextField(15);
    private final JTextField lastnameInputField = new JTextField(15);
    private final JTextField ageInputField = new JTextField(15);
    private final JTextField occupationInputField = new JTextField(15);
    private final JTextField addressInputField = new JTextField(40);
    private final JTextArea homeEviInputArea = new JTextArea(3, 20);
    private final JTextField telInputField = new JTextField(15);
    private final JTextField emailInputField = new JTextField(20);
    private final JTextArea reasonInputArea = new JTextArea(3, 20);
    private final JTextArea prevExpInputArea = new JTextArea(3, 20);
    private final JTextArea availabilityInputArea = new JTextArea(3, 20);
    private SubmitController submitController = null;

    private final JButton submit;
    private final JButton cancel;

    public SubmitView(SubmitViewModel submitViewModel) {
        this.submitViewModel = submitViewModel;
        submitViewModel.addPropertyChangeListener(this);

        this.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        final JLabel title = new JLabel(SubmitViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel note = new JLabel(SubmitViewModel.NOTE);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        note.setFont(new Font("Arial", Font.PLAIN, 15));

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
        homeEviInputArea.setLineWrap(true);
        homeEviInputArea.setWrapStyleWord(true);
        JScrollPane homeEviScroll = new JScrollPane(homeEviInputArea);
        homeEviInfo.add(new JLabel(SubmitViewModel.HOME_LABEL));
        homeEviInfo.add(homeEviScroll);
        homeEviInfo.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        final JPanel reasonInfo = new JPanel();
        reasonInfo.setLayout(new BoxLayout(reasonInfo, BoxLayout.Y_AXIS));
        reasonInputArea.setLineWrap(true);
        reasonInputArea.setWrapStyleWord(true);
        JScrollPane reasonScroll = new JScrollPane(reasonInputArea);
        reasonInfo.add(new JLabel(SubmitViewModel.REASON_LABEL));
        reasonInfo.add(reasonScroll);
        reasonInfo.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        final JPanel prevExpInfo = new JPanel();
        prevExpInfo.setLayout(new BoxLayout(prevExpInfo, BoxLayout.Y_AXIS));
        prevExpInputArea.setLineWrap(true);
        prevExpInputArea.setWrapStyleWord(true);
        JScrollPane prevExpScroll = new JScrollPane(prevExpInputArea);
        prevExpInfo.add(new JLabel(SubmitViewModel.EXP_LABEL));
        prevExpInfo.add(prevExpScroll);
        prevExpInfo.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        final JPanel availabilityInfo = new JPanel();
        availabilityInfo.setLayout(new BoxLayout(availabilityInfo, BoxLayout.Y_AXIS));
        availabilityInputArea.setLineWrap(true);
        availabilityInputArea.setWrapStyleWord(true);
        JScrollPane availabilityScroll = new JScrollPane(availabilityInputArea);
        availabilityInfo.add(new JLabel(SubmitViewModel.AVAILABILITY_LABEL));
        availabilityInfo.add(availabilityScroll);
        availabilityInfo.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

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
        this.add(Box.createVerticalStrut(3));
        this.add(note);
        this.add(Box.createVerticalStrut(10));
        this.add(usernameInfo);
        this.add(firstnameInfo);
        this.add(lastnameInfo);
        this.add(ageInfo);
        this.add(occupationInfo);
        this.add(telInfo);
        this.add(emailInfo);
        this.add(addressInfo);
        this.add(homeEviInfo);
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
        homeEviInputArea.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setHomeEvi(homeEviInputArea.getText());
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
        reasonInputArea.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setReason(reasonInputArea.getText());
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
        prevExpInputArea.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setPrevExp(prevExpInputArea.getText());
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
        availabilityInputArea.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SubmitState currentState = submitViewModel.getState();
                currentState.setAvailability(availabilityInputArea.getText());
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
        //updating the error or success message in submit state
        final SubmitState state = (SubmitState) evt.getNewValue();

        usernameInputField.setText(state.getUsername());
        firstnameInputField.setText(state.getFirstname());
        lastnameInputField.setText(state.getLastname());
        ageInputField.setText(state.getAge());
        occupationInputField.setText(state.getOccupation());
        addressInputField.setText(state.getAddress());
        homeEviInputArea.setText(state.getHomeEvi());
        telInputField.setText(state.getTel());
        emailInputField.setText(state.getEmail());
//
        reasonInputArea.setText(state.getReason());
        prevExpInputArea.setText(state.getPrevExp());
        availabilityInputArea.setText(state.getAvailability());
//
        if (state.getError() != null && !state.getError().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getError());
            state.setError(null);
        }
        if (state.getSuccess() != null && !state.getSuccess().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getSuccess());
            submitViewModel.resetState();
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setSubmitController(SubmitController controller) {
        this.submitController = controller;
        this.submitController.autoFill();
    }

//    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(() -> {
//            SubmitViewModel viewModel = new SubmitViewModel();
//
//            SubmitView submitView = new SubmitView(viewModel);
//
//            JFrame frame = new JFrame("Submit View Preview");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setContentPane(submitView);
//            frame.pack();
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
}
