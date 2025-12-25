package interface_adapter.delete_pet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DeletePetView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Delete Pet";

    private final JTextField idInputField = new JTextField(1);

    private final JButton submitButton = new JButton("Delete Pet");
    private final JButton cancelButton = new JButton("Cancel");

    private DeletePetController deletePetController;
    private DeletePetViewModel viewModel;

    public DeletePetView() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        JPanel buttons = new JPanel();
        formPanel.setLayout(new GridLayout(1, 2, 5,0));
        //formPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 10));

        formPanel.add(new JLabel("Please Enter I.D. of the Pet you want to delete:"));
        formPanel.add(new JLabel("Pet I.D:"));
        formPanel.add(idInputField);

        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttons.add(submitButton);
        buttons.add(cancelButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttons, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == submitButton) {
            // Add Pet
            if (deletePetController != null) {
                deletePetController.execute(
                        idInputField.getText()
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

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {

            case "petPreview":
                JOptionPane.showMessageDialog(this,
                        "Pet found:\n" + evt.getNewValue(),
                        "Pet Preview",
                        JOptionPane.INFORMATION_MESSAGE);
                break;

            case "deleteWarning":
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        evt.getNewValue().toString(),
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.NO_OPTION) {
                    return; // cancel delete
                }
                break;

            case "deleteStatus":
                JOptionPane.showMessageDialog(this, evt.getNewValue().toString());
                break;
        }

    }

    public String getViewName() {
        return viewName;
    }

    public void setViewModel(DeletePetViewModel deletePetViewModel) {
        this.viewModel = deletePetViewModel;
        this.viewModel.addPropertyChangeListener(this);
    }

    public void setDeletePetController(DeletePetController deletePetController) {
        this.deletePetController = deletePetController;
    }
}
