package view;

import entity.Application;
import interface_adapter.manage_application.ManageApplicationController;
import interface_adapter.manage_application.ManageApplicationViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import interface_adapter.ViewManagerModel;

public class ManageApplicationView extends JPanel implements PropertyChangeListener {

    private final ManageApplicationController controller;
    private final ManageApplicationViewModel viewModel;
    private ViewManagerModel viewManagerModel;

    private final JPanel cardContainer;

    public ManageApplicationView(ManageApplicationController controller,
                                 ManageApplicationViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout());

        // Added Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            if (viewManagerModel != null) {
                viewManagerModel.setState("home");
                viewManagerModel.firePropertyChange();
            }
        });
        add(backButton, BorderLayout.NORTH);


        // scrollable container
        cardContainer = new JPanel();
        cardContainer.setLayout(new BoxLayout(cardContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        // listen for updates
        viewModel.addPropertyChangeListener(this);

        // initial load
        controller.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("applications".equals(evt.getPropertyName())) {
            List<Application> apps = (List<Application>) evt.getNewValue();
            updateCards(apps);
        }
    }

    private void updateCards(List<Application> applications) {
        cardContainer.removeAll();

        for (Application app : applications) {
            JPanel card = createApplicationCard(app);
            cardContainer.add(card);
            cardContainer.add(Box.createVerticalStrut(10));
        }

        cardContainer.revalidate();
        cardContainer.repaint();
    }

    private JPanel createApplicationCard(Application app) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));



        panel.add(new JLabel("Application ID: " + app.getApplicationId()));
        panel.add(new JLabel("Pet ID: " + app.getPetId()));

        panel.add(new JLabel("First Name: " + app.getFirstName()));
        panel.add(new JLabel("Last Name: " + app.getLastName()));
        panel.add(new JLabel("Email: " + app.getEmail()));
        panel.add(new JLabel("Phone: " + app.getPhoneNumber()));
        panel.add(new JLabel("Age: " + app.getAge()));
        panel.add(new JLabel("Occupation: " + app.getOccupation()));

        panel.add(new JLabel("Reason: " + app.getReason()));
        panel.add(new JLabel("Environment: " + app.getHomeEnvironment()));
        panel.add(new JLabel("Availability: " + app.getAvailability()));
        panel.add(new JLabel("Previous Experience: " + app.getPreviousExperience()));

        panel.add(new JLabel("Status: " + app.getStatus()));

        return panel;
    }

    public String getViewName() {
        return "Manage Application";
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

}

