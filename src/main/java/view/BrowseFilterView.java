package view;

import interface_adapter.browse_filter.BrowseFilterController;
import interface_adapter.browse_filter.BrowseFilterState;
import interface_adapter.browse_filter.BrowseFilterViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class BrowseFilterView extends JFrame implements PropertyChangeListener {

    private final BrowseFilterController controller;
    private final BrowseFilterViewModel viewModel;

    private final JTextField speciesField = new JTextField(20);
    private final JTextField genderField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");
    private final JButton clearButton = new JButton("Clear Filters");

    // TABLE version
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Name", "Type", "Breed", "Gender"}, 0
    );
    private final JTable petTable = new JTable(tableModel);

    public BrowseFilterView(BrowseFilterController controller, BrowseFilterViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        viewModel.addPropertyChangeListener(this);

        setTitle("Pet Adoption Portal - Browse & Filter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // ===== Filter Panel =====
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Species:"), gbc);

        gbc.gridx = 1;
        filterPanel.add(speciesField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Gender:"), gbc);

        gbc.gridx = 1;
        filterPanel.add(genderField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);
        filterPanel.add(buttonPanel, gbc);

        // ===== TABLE results with scroll =====
        JScrollPane scrollPane = new JScrollPane(petTable);
        petTable.setRowHeight(28);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(filterPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // ===== Button Actions =====
        searchButton.addActionListener(e -> {
            String species = speciesField.getText().trim();
            String gender = genderField.getText().trim();
            controller.execute(species, gender);
        });

        clearButton.addActionListener(e -> {
            speciesField.setText("");
            genderField.setText("");
//            controller.execute("", ""); // reload all

            BrowseFilterState state = viewModel.getState();
            state.setPets(List.of());
            viewModel.firePropertyChange("pets");
        });

        setVisible(true);
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
            // "name | type | breed | gender"
            String[] data = line.split("\\|");
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i].trim();
            }
            tableModel.addRow(data);
        }
    }
}

