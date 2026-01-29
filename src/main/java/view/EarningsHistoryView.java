package view;

import entity.EarningsRecord;
import interface_adapter.earnings_history.EarningsHistoryController;
import interface_adapter.earnings_history.EarningsHistoryState;
import interface_adapter.earnings_history.EarningsHistoryViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class EarningsHistoryView extends JPanel implements PropertyChangeListener {

    private final String viewName = "earnings history";
    private final EarningsHistoryController controller;
    private final EarningsHistoryViewModel viewModel;

    private final JTextField symbolField = new JTextField("", 25);
    private final JButton loadButton = new JButton("Load Earnings");
    private final JButton backButton = new JButton("Back");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Period", "Actual", "Estimate", "Surprise"}, 0
    );
    private final JTable table = new JTable(tableModel);

    public EarningsHistoryView(EarningsHistoryController controller,
                               EarningsHistoryViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Company Earnings"));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Company Symbol:"));
        topPanel.add(symbolField);
        topPanel.add(loadButton);
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadButton.addActionListener(e ->
                controller.onLoadButtonClicked(symbolField.getText()));

        loadButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    public String getViewName() {
        return viewName;
    }

    // AppBuilder calls this so the Back button knows where to go
    public void setBackNavigation(ViewManagerModel viewManagerModel,
                                  String loggedInViewName) {
        backButton.addActionListener(e -> {
            viewManagerModel.setState(loggedInViewName);
            viewManagerModel.firePropertyChange();
        });
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        EarningsHistoryState state = (EarningsHistoryState) evt.getNewValue();

        loadButton.setEnabled(!state.isLoading());
        updateTable(state.getRecords());
        showMessageIfAny(state);
    }

    private void updateTable(List<EarningsRecord> records) {
        tableModel.setRowCount(0);
        for (EarningsRecord r : records) {
            tableModel.addRow(new Object[]{
                    r.getPeriod(),
                    r.getActual() != null ? String.format("%.3f", r.getActual()) : "N/A",
                    r.getEstimate() != null ? String.format("%.3f", r.getEstimate()) : "N/A",
                    r.getSurprise() != null ? String.format("%.3f", r.getSurprise()) : "N/A"
            });
        }
    }

    private void showMessageIfAny(EarningsHistoryState state) {
        String msg = state.getMessage();
        if (msg == null || msg.isEmpty()) return;

        int type;
        String title;
        switch (state.getMessageType()) {
            case WARNING -> {
                type = JOptionPane.WARNING_MESSAGE;
                title = "No Data";
            }
            case ERROR -> {
                type = JOptionPane.ERROR_MESSAGE;
                title = "Error";
            }
            case INFO -> {
                type = JOptionPane.INFORMATION_MESSAGE;
                title = "Info";
            }
            default -> {
                type = JOptionPane.PLAIN_MESSAGE;
                title = "Info";
            }
        }

        JOptionPane.showMessageDialog(this, msg, title, type);
    }
}
