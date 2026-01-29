package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.loyalty_score.LoyaltyController;
import interface_adapter.loyalty_score.LoyaltyViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * The View for looking at user's loyalty score
 */
public class LoyaltyScoreView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "loyalty";
    private JTable loyaltyScoreTable;
    private JButton backButton;
    private final LoyaltyViewModel loyaltyViewModel;
    private final ViewManagerModel viewManagerModel;
    public LoyaltyController loyaltyController;

    // Constructor to initialize the view
    public LoyaltyScoreView(LoyaltyViewModel loyaltyViewModel, ViewManagerModel viewManagerModel) {
        this.loyaltyViewModel = loyaltyViewModel;
        this.viewManagerModel = viewManagerModel;

        this.loyaltyViewModel.addPropertyChangeListener(this);
        this.viewManagerModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // Initialize components
        initializeComponents();

    }

    private void initializeComponents() {
        // Create a panel for the artist name at the top
        JPanel artistPanel = new JPanel(new BorderLayout());
        JLabel artistNameLabel = new JLabel(loyaltyViewModel.getState().getCurrentArtist() + " Loyalty Scores", JLabel.CENTER);
        artistNameLabel.setFont(new Font("Calibri", Font.BOLD, 30));
        artistPanel.setPreferredSize(new Dimension(400, 100));
        artistPanel.add(artistNameLabel, BorderLayout.CENTER);

        // ADD INFO BUTTON
        JButton infoButton = new JButton("ℹ How is the score calculated?");
        infoButton.setFont(new Font("Calibri", Font.PLAIN, 14));
        infoButton.setFocusPainted(false);
        infoButton.addActionListener(e -> showFormulaExplanation());
        System.out.println("Info button created");

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.add(infoButton);
        artistPanel.add(infoPanel, BorderLayout.EAST);

        add(artistPanel, BorderLayout.NORTH);

        // resizing
        artistPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = artistPanel.getWidth();
                int h = artistPanel.getHeight();

                artistPanel.setPreferredSize(new Dimension(
                        w,                         // full width
                        (int)(getHeight() * 0.15)  // 15% of total window height
                ));

                artistPanel.revalidate();
            }
        });

        // Create the table and update its data
        loyaltyScoreTable = new JTable();
        updateTableData(); // Set initial table data

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        loyaltyScoreTable.setDefaultRenderer(Object.class, centerRenderer);

        loyaltyScoreTable.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 14));
        loyaltyScoreTable.setFont(new Font("Verdana", Font.PLAIN, 16));
        loyaltyScoreTable.setRowHeight(30);
        // TODO: FIX for null
        // loyaltyScoreTable.getColumnModel().getColumn(0).setPreferredWidth(20);

        // Create pane to hold table

        JScrollPane scrollPane = new JScrollPane(loyaltyScoreTable);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scrollPane.setPreferredSize(new Dimension(800, 750));
        wrapper.add(scrollPane);

        // dynamically resizing
        wrapper.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = wrapper.getWidth();
                int h = wrapper.getHeight();

                scrollPane.setPreferredSize(new Dimension(
                        (int)(w * 0.80),
                        (int)(h * 0.95)
                ));

                wrapper.revalidate();
            }
        });

        add(wrapper, BorderLayout.CENTER);

        // Create back button
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(this);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(200, 60));
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH); // Move back button to the bottom
    }

    // Update the table data with loyalty scores
    private void updateTableData() {
        Map<String, Integer> scores = loyaltyViewModel.getState().getLoyaltyScores();
        ArrayList<String> dates = loyaltyViewModel.getState().getDates();

        if ( dates == null){ return; }

        dates.sort(Comparator.reverseOrder());
        String[] columnNames = {"Previous Visit Date", "Loyalty Score"};

        Object[][] data = new Object[dates.size()][2];

        for (int i = 0; i < dates.size(); i++) {
            String date = dates.get(i);
            data[i][0] = date;

            if (scores.containsKey(date)) {
                data[i][1] = scores.get(date);
            } else {
                data[i][1] = ":( No score available for this date";
            }
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        loyaltyScoreTable.setModel(model);
    }

    // Handle actions (e.g., back button click)
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
            // for debug
            System.out.println("back button pressed!");
        }
    }

    // PropertyChangeListener to update view when the state changes
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Update the table with the latest loyalty scores
        updateTableData();

        // Update the artist name in the existing panel
        JPanel artistPanel = (JPanel) getComponent(0);

        // Find and update just the center component (the artist name label)
        Component centerComponent = ((BorderLayout) artistPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComponent instanceof JLabel) {
            JLabel artistNameLabel = (JLabel) centerComponent;
            artistNameLabel.setText("Loyalty Scores for: " + loyaltyViewModel.getState().getCurrentArtist());
            artistNameLabel.setFont(new Font("Calibri", Font.BOLD, 30));  // Keep consistent font
        }

        artistPanel.revalidate();
        artistPanel.repaint();
    }

    public void setLoyaltyController(LoyaltyController loyaltyController) {
        this.loyaltyController = loyaltyController;
    }

    public String getViewName() {
        return viewName;
    }
    /**
     * Shows a dialog explaining the loyalty score calculation formula
     */
    private void showFormulaExplanation() {
        String explanation = "<html>" +
                "<body style='width: 400px; padding: 10px;'>" +
                "<h2 style='color: #1DB954;'>Loyalty Score Formula</h2>" +
                "<p>Your loyalty score for an artist is calculated based on:</p>" +
                "<ul>" +
                "<li><b>Saved Tracks:</b> 10 points per track</li>" +
                "<li><b>Saved Albums:</b> 50 points per album</li>" +
                "<li><b>Recently Played:</b> 100 point bonus if you've listened recently</li>" +
                "</ul>" +
                "<h3>Example Calculation:</h3>" +
                "<p style='background-color: #f0f0f0; padding: 10px; border-radius: 5px;'>" +
                "If you have:<br>" +
                "• 5 saved tracks (5 × 10 = 50 points)<br>" +
                "• 2 saved albums (2 × 50 = 100 points)<br>" +
                "• Listened recently (+100 points)<br>" +
                "<br>" +
                "<b>Total Score: 250 points</b>" +
                "</p>" +
                "<p style='margin-top: 15px;'>" +
                "<i>Higher scores indicate stronger loyalty to the artist!</i>" +
                "</p>" +
                "</body>" +
                "</html>";

        JOptionPane.showMessageDialog(
                this,
                explanation,
                "Loyalty Score Calculation",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}