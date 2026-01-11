package view.group_analytics;

import entity.UserTasteProfile;
import interface_adapter.ViewManagerModel;
import interface_adapter.group_analytics.GroupAnalyticsController;
import interface_adapter.group_analytics.GroupAnalyticsState;
import interface_adapter.group_analytics.GroupAnalyticsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;

/**
 * Simple view showing the verdict and scores.
 * For now it uses a demo group; your teammate's group-forming use case
 * should call controller.analyzeGroup(...) with real UserTasteProfiles.
 */
public class GroupAnalyticsView extends JPanel implements ActionListener, PropertyChangeListener {

    private final GroupAnalyticsViewModel viewModel;
    private GroupAnalyticsController controller;
    //Added these
    private final ViewManagerModel viewManagerModel;
    private final JTextArea outputArea = new JTextArea(15, 40);
    private final JButton backButton = new JButton("Back to Group");

    public void setGroupAnalyticsController(GroupAnalyticsController controller) {
        this.controller = controller;
    }

    public GroupAnalyticsView(GroupAnalyticsViewModel viewModel,
                              ViewManagerModel viewManagerModel) {

        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Group Analytics", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        bottom.add(backButton);
        backButton.addActionListener(this);

        add(top, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }


    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == backButton) {
            //Go back to InGroupView
            viewManagerModel.setState("in group");
            viewManagerModel.firePropertyChange();
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) return;
        GroupAnalyticsState state = (GroupAnalyticsState) evt.getNewValue();

        if (state.getErrorMessage() != null) {
            outputArea.setText("Error: " + state.getErrorMessage());
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Final verdict: ").append(state.getVerdict()).append("\n\n");
        sb.append(String.format("Average similarity: %.2f%n%n", state.getAverageSimilarity()));

        sb.append("Vibe scores:\n");
        if (state.getVibeScores() != null) {
            state.getVibeScores().forEach((k, v) ->
                    sb.append("  ").append(k).append(": ")
                            .append(String.format("%.2f", v))
                            .append("\n")
            );
        }

        sb.append("\nPairwise similarities:\n");
        if (state.getPairwise() != null) {
            state.getPairwise().forEach(p ->
                    sb.append(String.format("  %s ↔ %s : %.2f%n",
                            p.getUserA(), p.getUserB(), p.getSimilarity()))
            );
        }

        outputArea.setText(sb.toString());
    }

    public String getViewName() {
        return viewModel.getViewName();
    }

}