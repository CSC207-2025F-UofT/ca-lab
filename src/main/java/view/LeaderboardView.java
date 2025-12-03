package view;

import entity.User;
import interface_adapter.leaderboard.*;
import interface_adapter.back.BackController;
import interface_adapter.themes.*;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.beans.*;

/**
 * Leaderboard view.
 */
public class LeaderboardView extends JPanel implements PropertyChangeListener, ThemedView {

    private static final String VIEW_NAME = "leaderboard";

    private final LeaderboardViewModel leaderboardViewModel;
    private LeaderboardController leaderboardController = null;
    private BackController backController = null;
    private final ThemeManager themeManager;

    private final JPanel leaderboardTable = new JPanel();
    private final JPanel navBar = new JPanel();
    private final JLabel pageLabel = new JLabel();

    public LeaderboardView(
            LeaderboardViewModel leaderboardViewModel,
            ThemeManager themeManager
    ) {
        this.leaderboardViewModel = leaderboardViewModel;
        leaderboardViewModel.addPropertyChangeListener(this);

        // Theme manager:
        this.themeManager = themeManager;
        themeManager.registerView(this);

        setLayout(new BorderLayout());


        // Components:

        // Top bar — title and back button:
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.Y_AXIS));
        topBar.setBorder(BorderFactory.createEmptyBorder(
                10, 10, 10, 10)
        );

        JLabel titleLabel = new JLabel("Leaderboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        topBar.add(titleLabel);

        JButton backButton = new JButton("Back");
        topBar.add(backButton);

        add(topBar, BorderLayout.NORTH);

        // Leaderboard table:
        JPanel leaderboardPanel = new JPanel(new GridBagLayout());

        leaderboardTable.setLayout(
                new BoxLayout(leaderboardTable, BoxLayout.Y_AXIS)
        );
        leaderboardTable.setBorder(
                BorderFactory.createLineBorder(Color.GRAY)
        );

        leaderboardPanel.add(leaderboardTable);
        add(leaderboardPanel, BorderLayout.CENTER);

        // Bottom bar — update button and page navigations:
        JPanel botBar = new JPanel();
        botBar.setLayout(new BoxLayout(botBar, BoxLayout.Y_AXIS));

        JButton updateButton = new JButton("Update");
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton previousButton = new JButton("<");
        JButton nextButton = new JButton(">");
        navBar.add(previousButton);
        navBar.add(pageLabel);
        navBar.add(nextButton);
        navBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        botBar.add(updateButton);
        botBar.add(navBar);
        add(botBar, BorderLayout.SOUTH);


        // Event connections:

        // Back button:
        backButton.addActionListener(e -> {

            // If currently not on page 1, preemptively reset to page 1.
            if (leaderboardViewModel.getState().getPage() != 1) {
                leaderboardController.changePage(1);
            }

            backController.execute();
        });

        // Update button:
        updateButton.addActionListener(e -> {
            leaderboardController.changePage(
                    leaderboardViewModel.getState().getPage()
            );
        });

        // Previous button:
        previousButton.addActionListener(e -> {
            leaderboardController.changePage(
                    leaderboardViewModel.getState().getPage() - 1
            );
        });

        // Next button:
        nextButton.addActionListener(e -> {
            leaderboardController.changePage(
                    leaderboardViewModel.getState().getPage() + 1
            );
        });
    }

    public String getViewName() { return VIEW_NAME; }

    /**
     * Set the leaderboard and back controllers for this view.
     * @param leaderboardController leaderboard controller.
     * @param backController back controller.
     */
    public void setControllers(
            LeaderboardController leaderboardController,
            BackController backController
    ) {
        this.leaderboardController = leaderboardController;
        this.backController = backController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

        LeaderboardState state = leaderboardViewModel.getState();
        ArrayList<Object[]> userRankPairs = state.getUserRankPairs();

        // Clear the leaderboard panel.
        leaderboardTable.removeAll();

        // If the sublist of user-rank pairs is empty,
        // display a message and hide the navigation panel.
        if (userRankPairs.isEmpty()) {
            leaderboardTable.add(new JLabel(
                    "No users found. 🧐"
            ));
            navBar.setVisible(false);
        }

        // Otherwise, there are entries to display,
        // so update the view as usual.
        else {
            // Update the leaderboard table.
            updateLeaderboard(userRankPairs);

            // Update the page label.
            pageLabel.setText(Integer.toString(
                    state.getPage()
            ));

            navBar.setVisible(true);
        }

        // Apply the theme.
        applyTheme(themeManager.getActiveTheme());
    }

    /**
     * Repopulate the leaderboard panel with a new sublist of user-rank pairs.
     * @param userRankPairs sublist of user-rank pairs to display.
     */
    private void updateLeaderboard(ArrayList<Object[]> userRankPairs) {

        // Column headers:
        leaderboardTable.add(constructRow(
                "Rank", "User", "Score"
        ));

        // Create rows with the given user-rank pairs.
        for (Object[] pair : userRankPairs) {
            User user = (User) pair[0];
            int rank = (int) pair[1];

            // Construct the row with the given
            // rank, user name, and user score.
            leaderboardTable.add(constructRow(
                    Integer.toString(rank),
                    user.getName(),
                    Integer.toString(user.getScore())
            ));
        }

        // Format the leaderboard.
        scaleColumns();

        leaderboardTable.revalidate();
        leaderboardTable.repaint();

        System.out.println("Successfully updated the leaderboard to page " +
                leaderboardViewModel.getState().getPage());
    }

    // Border of each cell.
    private static final Border CELL_BORDER = BorderFactory.createMatteBorder(
            1,1,1,1, Color.GRAY
    );
    // Inset constraint for each cell.
    private static final GridBagConstraints CELL_INSETS_CONSTRAINT = new GridBagConstraints();
    static {
        CELL_INSETS_CONSTRAINT.insets = new Insets(
                5, 5, 5, 5
        );
    }
    /**
     * Construct a leaderboard row with the given rank and user object.
     * Uses table format.
     * @param rank rank of the user.
     * @param name name of the user.
     * @param score score of the user.
     * @return constructed leaderboard row to add to the table.
     */
    private JPanel constructRow(String rank, String name, String score) {

        // Initialise the row.
        JPanel row = new JPanel(new GridBagLayout());

        // Rank:
        JPanel rankPanel = new JPanel();
        rankPanel.setBorder(CELL_BORDER);

        rankPanel.add(
                new JLabel(rank, SwingConstants.CENTER),
                CELL_INSETS_CONSTRAINT
        );
        row.add(rankPanel);

        // Username:
        JPanel namePanel = new JPanel();
        namePanel.setBorder(CELL_BORDER);

        namePanel.add(
                new JLabel(name),
                CELL_INSETS_CONSTRAINT
        );
        row.add(namePanel);

        // Score:
        JPanel scorePanel = new JPanel();
        scorePanel.setBorder(CELL_BORDER);

        scorePanel.add(
                new JLabel(score, SwingConstants.CENTER),
                CELL_INSETS_CONSTRAINT
        );
        row.add(scorePanel);

        return row;
    }

    /**
     * Scale the width of every cell in the leaderboard table
     * to the widest cell in that column.
     */
    private void scaleColumns() {

        // Calculate the maximum widths of cells for each column.
        int[] maxWidths = new int[3];

        for (Component row : leaderboardTable.getComponents()) {
            Component[] cell = ((JPanel) row).getComponents();

            // Record the max of the current recorded width or the
            // current cell's width.
            for (int i = 0; i < cell.length; i++) {
                maxWidths[i] = Math.max(
                        maxWidths[i],
                        cell[i].getPreferredSize().width
                );
            }
        }

        // Apply the recorded max width to each column.
        for (Component row : leaderboardTable.getComponents()) {
            Component[] cells = ((JPanel) row).getComponents();

            for (int i = 0; i < cells.length; i++) {
                cells[i].setPreferredSize(new Dimension(
                        maxWidths[i],
                        cells[i].getPreferredSize().height
                ));
            }
        }
    }

    public void applyTheme(Theme theme) {
        ThemeUtil.applyTheme(this, theme);
    }

}