package view;

import interface_adapter.view_pet_details.ViewPetDetailsViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewPetDetailsView extends JFrame implements PropertyChangeListener {

    private final ViewPetDetailsViewModel viewModel;

    private final JLabel nameLabel = new JLabel();
    private final JLabel typeLabel = new JLabel();
    private final JLabel breedLabel = new JLabel();
    private final JLabel ageLabel = new JLabel();
    private final JLabel genderLabel = new JLabel();
    private final JLabel sizeLabel = new JLabel();
    private final JLabel contactLabel = new JLabel();

    public ViewPetDetailsView(ViewPetDetailsViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        setTitle("Pet Details");
        setSize(400, 350);
        setLayout(new GridLayout(7, 1, 5, 5));
        setLocationRelativeTo(null);

        add(nameLabel);
        add(typeLabel);
        add(breedLabel);
        add(ageLabel);
        add(genderLabel);
        add(sizeLabel);
        add(contactLabel);

        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("petDetails".equals(evt.getPropertyName())) {
            nameLabel.setText("Name: " + viewModel.getPetName());
            typeLabel.setText("Type: " + viewModel.getPetType());
            breedLabel.setText("Breed: " + viewModel.getPetBreed());
            ageLabel.setText("Age: " + viewModel.getPetAge());
            genderLabel.setText("Gender: " + viewModel.getPetGender());
            sizeLabel.setText("Size: " + viewModel.getPetSize());
            contactLabel.setText("Contact: " + viewModel.getPetContact());
        }
    }
}
