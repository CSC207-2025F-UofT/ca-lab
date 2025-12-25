package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.browse_filter.BrowseFilterController;
import interface_adapter.browse_filter.BrowseFilterState;
import interface_adapter.browse_filter.BrowseFilterViewModel;
import interface_adapter.view_pet_details.ViewPetDetailsController;
import interface_adapter.view_pet_details.ViewPetDetailsViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class BrowseFilterView extends JPanel implements PropertyChangeListener {

    private BrowseFilterController controller;
    private final BrowseFilterViewModel viewModel;

    private ViewPetDetailsController detailsController;
    private final ViewPetDetailsViewModel detailsViewModel;
    private ViewPetDetailsView viewPetDetailsView;

    private final ViewManagerModel viewManagerModel; // for back navigation

    private final JTextField speciesField = new JTextField(20);
    private final JTextField genderField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");
    private final JButton clearButton = new JButton("Clear Filters");
    private final JButton backButton = new JButton("Back"); // NEW

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Type", "Breed", "Gender"}, 0
    );
    private final JTable petTable = new JTable(tableModel);

    public BrowseFilterView(BrowseFilterViewModel viewModel,
                            ViewPetDetailsViewModel detailsViewModel,
                            ViewManagerModel viewManagerModel) {

        this.viewModel = viewModel;
        this.detailsViewModel = detailsViewModel;
        this.viewManagerModel = viewManagerModel;

        viewModel.addPropertyChangeListener(this);

        // ========== Layout & Panels ==========
        setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Species label + field
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Species:"), gbc);

        gbc.gridx = 1;
        filterPanel.add(speciesField, gbc);

        // Gender label + field
        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Gender:"), gbc);

        gbc.gridx = 1;
        filterPanel.add(genderField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);   // NEW
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        filterPanel.add(buttonPanel, gbc);

        // Table
        JScrollPane scrollPane = new JScrollPane(petTable);
        petTable.setRowHeight(28);

        // Hide ID column
        petTable.getColumnModel().getColumn(0).setMinWidth(0);
        petTable.getColumnModel().getColumn(0).setMaxWidth(0);
        petTable.getColumnModel().getColumn(0).setWidth(0);

        // Add to main panel
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ========== Button Actions ==========

        searchButton.addActionListener(e -> {
            String species = speciesField.getText().trim();
            String gender = genderField.getText().trim();
            controller.execute(species, gender);
        });

        clearButton.addActionListener(e -> {
            speciesField.setText("");
            genderField.setText("");
            BrowseFilterState state = viewModel.getState();
            state.setPets(List.of());
            viewModel.firePropertyChange("pets");
        });

        // NEW: Back → go to "home" card
        backButton.addActionListener(e -> {
            viewManagerModel.setState("home");
            viewManagerModel.firePropertyChange();
        });

        // ========== Row Click Listener (unchanged) ==========
        petTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = petTable.getSelectedRow();
                if (row != -1) {
                    String petId = tableModel.getValueAt(row, 0).toString();

                    // your original behavior:
                    detailsController.execute(petId);
                    viewPetDetailsView.setVisible(true);
                }
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("pets".equals(evt.getPropertyName())) {
            updateTable(viewModel.getState().getPets());
        }
    }

    private void updateTable(List<String> pets) {
        tableModel.setRowCount(0);

        for (String line : pets) {
            String[] data = line.split("\\|");
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i].trim();
            }
            tableModel.addRow(data);
        }
    }

    public String getViewName() {
        return "browse and filter";
    }

    public void setBrowseFilterController(BrowseFilterController controller) {
        this.controller = controller;
    }

    public void setDetailsController(ViewPetDetailsController detailsController) {
        this.detailsController = detailsController;
    }

    public void setViewPetDetailsView(ViewPetDetailsView viewPetDetailsView) {
        this.viewPetDetailsView = viewPetDetailsView;
    }
}
