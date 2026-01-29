package view;

import interface_adapter.submit_application.SubmitController;
import interface_adapter.submit_application.SubmitState;
import interface_adapter.submit_application.SubmitViewModel;
import interface_adapter.view_pet_details.ViewPetDetailsController;
import interface_adapter.view_pet_details.ViewPetDetailsState;
import interface_adapter.view_pet_details.ViewPetDetailsViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewPetDetailsView extends JFrame implements PropertyChangeListener {

    private final ViewPetDetailsViewModel viewModel;
    private SubmitViewModel submitViewModel;
    private final String viewName = "view pet details";
    private ViewPetDetailsController viewPetDetailsController;
    private SubmitController submitController;

    private final JLabel nameLabel = new JLabel();
    private final JLabel typeLabel = new JLabel();
    private final JLabel breedLabel = new JLabel();
    private final JLabel ageLabel = new JLabel();
    private final JLabel genderLabel = new JLabel();
    private final JLabel sizeLabel = new JLabel();
    private final JLabel contactLabel = new JLabel();
    private final JButton adoptButton = new JButton("I want to adopt this pet");

    public void setViewPetDetailsController(ViewPetDetailsController controller) {
        this.viewPetDetailsController = controller;
    }
    public void setSubmitController(SubmitController submitController) {
        this.submitController = submitController;
    }
    public void setSubmitViewModel(SubmitViewModel submitViewModel) { this.submitViewModel = submitViewModel; }

    public ViewPetDetailsView(ViewPetDetailsViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        setTitle("Pet Details");
        setSize(400, 350);
        setLayout(new GridLayout(8, 1, 5, 5));
        setLocationRelativeTo(null);

        add(nameLabel);
        add(typeLabel);
        add(breedLabel);
        add(ageLabel);
        add(genderLabel);
        add(sizeLabel);
        add(contactLabel);
        add(adoptButton);

        adoptButton.addActionListener(e -> {
            SubmitState currentState = submitViewModel.getState();
            currentState.setPetId(viewModel.getState().getPetId());
            System.out.println("pet_id sent from viewpetdetail: " + viewModel.getState().getPetId());
            submitViewModel.setState(currentState);
            System.out.println("pet_id from submitstate: " + currentState.getPetId());
            submitController.autoFill();
            viewPetDetailsController.switchToApplicationView();
            System.out.println("pet_id after switchToApplicationView: " + currentState.getPetId());

            dispose();
        });

        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            ViewPetDetailsState state = (ViewPetDetailsState) evt.getNewValue();

            if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
                JOptionPane.showMessageDialog(this, state.getErrorMessage());
            } else {
                nameLabel.setText("Name: " + state.getPetName());
                typeLabel.setText("Type: " + state.getPetType());
                breedLabel.setText("Breed: " + state.getPetBreed());
                ageLabel.setText("Age: " + state.getPetAge());
                genderLabel.setText("Gender: " + state.getPetGender());
                sizeLabel.setText("Size: " + state.getPetSize());
                contactLabel.setText("Contact: " + state.getPetContact());
            }
        }
    }

    public String getViewName() {
        return viewName;
    }
}
