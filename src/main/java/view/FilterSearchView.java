package view;

import data_access.FilterSearchDataAccessObject;
import entity.Stock;
import interface_adapter.filter_search.FilterSearchController;
import interface_adapter.filter_search.FilterSearchState;
import interface_adapter.filter_search.FilterSearchViewModel;
import use_case.filter_search.FilterSearchInputBoundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The View for when the user is in the Filter Search Page.
 */

public class FilterSearchView extends JPanel implements PropertyChangeListener {
    private FilterSearchController filterSearchController;
    private final FilterSearchViewModel filterSearchViewModel;

    // Table-related fields
    private JTable table;
    private DefaultTableModel tableModel;

    private static final int COL_SYMBOL = 0;
    private static final int COL_DESCRIPTION  = 1;
    private static final int COL_CURRENCY    = 2;
    private static final int COL_DISPLAY   = 3;
    private static final int COL_FIGI = 4;
    private static final int COL_MIC  = 5;
    private static final int COL_TYPE = 6;



    public FilterSearchView(FilterSearchViewModel filterSearchViewModel) {
        this.filterSearchViewModel = filterSearchViewModel;
        this.filterSearchViewModel.addPropertyChangeListener(this);

        final String[] exchangeOptions = FilterSearchInputBoundary.EXCHANGE_OPTIONS;
        final String[] micOptions = FilterSearchInputBoundary.MIC_OPTIONS;
        final String[] securityOptions = FilterSearchInputBoundary.SECURITY_OPTIONS;
        final String[] currencyOptions = FilterSearchInputBoundary.CURRENCY_OPTIONS;

        final JComboBox<String> exchangeDrop = new JComboBox<>(exchangeOptions);
        final JComboBox<String> micDrop = new JComboBox<>(micOptions);
        final JComboBox<String> securityDrop = new JComboBox<>(securityOptions);
        final JComboBox<String> currencyDrop = new JComboBox<>(currencyOptions);
        JButton search = new JButton("Search");

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backToHomeButton = new JButton("Back");
        leftPanel.add(backToHomeButton);
        topPanel.add(leftPanel, BorderLayout.WEST);

        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel panel_e = new JPanel();
        panel_e.add(new JLabel("Exchange"));
        panel_e.add(exchangeDrop);

        JPanel panel_m = new JPanel();
        panel_m.add(new JLabel("MIC:"));
        panel_m.add(micDrop);

        JPanel panel_s = new JPanel();
        panel_s.add(new JLabel("Security Type:"));
        panel_s.add(securityDrop);

        JPanel panel_c = new JPanel();
        panel_c.add(new JLabel("Currency:"));
        panel_c.add(currencyDrop);

        filtersPanel.add(panel_e);
        filtersPanel.add(panel_m);
        filtersPanel.add(panel_s);
        filtersPanel.add(panel_c);
        filtersPanel.add(search);

        topPanel.add(filtersPanel, BorderLayout.CENTER);

        String[] columnNames = {"Symbol", "Description", "Currency",
                "Display Symbol", "FIGI", "MIC", "Security Type"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(800, 400));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Home Button
        backToHomeButton.addActionListener(e -> {
            // TODO: implement navigation back to the main/home view.
            // For now, you can leave this empty or show a message:
            JOptionPane.showMessageDialog(
                    this,
                    "Back to Home is not implemented yet.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // Search Button
        search.addActionListener(e -> {

            String ex   = exchangeDrop.getSelectedItem() != null
                    ? exchangeDrop.getSelectedItem().toString() : "";
            String mi   = micDrop.getSelectedItem() != null
                    ? micDrop.getSelectedItem().toString() : "";
            String sec  = securityDrop.getSelectedItem() != null
                    ? securityDrop.getSelectedItem().toString() : "";
            String curr = currencyDrop.getSelectedItem() != null
                    ? currencyDrop.getSelectedItem().toString() : "";

            if (filterSearchController != null) {
                filterSearchController.loadStocks(ex, mi, sec, curr);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "FilterSearchController is not set.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }

    public String getViewName() {;
        return "Filter Search";
    }

    public void setFilterSearchController(FilterSearchController filterSearchController) {
        this.filterSearchController = filterSearchController;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        List<Stock> stocks = filterSearchViewModel.getStocks();
        System.out.println("VIEW propertyChange: stocks = " +
                (stocks == null ? "null" : stocks.size()));

        String error = filterSearchViewModel.getErrorMessage();

        if (error != null && !error.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    error,
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Update table model with new stocks
        tableModel.setRowCount(0); // clear old rows

        if (stocks != null) {
            for (Stock s : stocks) {
                Object[] row = new Object[7];
                row[COL_SYMBOL]      = s.getSymbol();
                row[COL_DESCRIPTION] = s.getDescription();
                row[COL_CURRENCY]    = s.getCurrency();
                row[COL_DISPLAY]     = s.getDisplaySymbol();
                row[COL_FIGI]        = s.getFigi();
                row[COL_MIC]         = s.getMic();
                row[COL_TYPE]        = s.getType();
                tableModel.addRow(row);
            }
        }
        System.out.println("TABLE rowCount = " + tableModel.getRowCount());
    }
}




