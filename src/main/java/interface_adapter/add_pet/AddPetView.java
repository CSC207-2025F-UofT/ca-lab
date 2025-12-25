package interface_adapter.add_pet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AddPetView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Add Pet";

    private final JTextField nameInputField = new JTextField(15);
    private final JTextField typeInputField = new JTextField(15);
    private final JTextField breedInputField = new JTextField(15);
    private final JTextField contactInputField = new JTextField(15);

    private final JComboBox<String> ageDropdown =
            new JComboBox<>(new String[]{"Baby", "Young", "Adult"});

    private final JComboBox<String> genderDropdown =
            new JComboBox<>(new String[]{"Male", "Female"});

    private final JComboBox<String> sizeDropdown =
            new JComboBox<>(new String[]{"Small", "Medium", "Large"});


    private final JButton submitButton = new JButton("Add Pet");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton clearButton = new JButton("Clear Fields");

    // The controller for this view
    private AddPetController addPetController;
    private AddPetViewModel viewModel;

    public AddPetView() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        JPanel buttons = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 5, 5));

        formPanel.add(new JLabel("Pet Name:"));
        formPanel.add(nameInputField);

        formPanel.add(new JLabel("Type:"));
        formPanel.add(typeInputField);

        formPanel.add(new JLabel("Breed:"));
        formPanel.add(breedInputField);

        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageDropdown);

        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderDropdown);

        formPanel.add(new JLabel("Size:"));
        formPanel.add(sizeDropdown);

        formPanel.add(new JLabel("Contact:"));
        formPanel.add(contactInputField);

        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);
        clearButton.addActionListener(this);

        buttons.add(submitButton);
        buttons.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(clearButton, BorderLayout.NORTH);
        add(buttons, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewName;
    }

    public void setAddPetController(AddPetController controller) {
        this.addPetController = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == submitButton) {
            // Add Pet
            if (addPetController != null) {
                addPetController.execute(
                        nameInputField.getText(),
                        typeInputField.getText(),
                        breedInputField.getText(),
                        ageDropdown.getSelectedItem().toString(),
                        genderDropdown.getSelectedItem().toString(),
                        sizeDropdown.getSelectedItem().toString(),
                        contactInputField.getText()
                );
            }
        }

        else if (source == cancelButton) {
            // Close the parent window
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.dispose();
            }
        }

        else if (source == clearButton) {
            // Clear all fields
            nameInputField.setText("");
            typeInputField.setText("");
            breedInputField.setText("");
            ageDropdown.setSelectedIndex(0);
            genderDropdown.setSelectedIndex(0);
            sizeDropdown.setSelectedIndex(0);
            contactInputField.setText("");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // This is where the presenter can update UI elements.
        if (evt.getPropertyName().equals("addPetStatus")) {
            JOptionPane.showMessageDialog(this, evt.getNewValue().toString());
        }
    }

    public void setViewModel(AddPetViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
    }
}
