package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.InGroupViewModel;
import interface_adapter.sharedsong.SharedSongViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class SharedSongView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "shared song";
    private InGroupViewModel inGroupViewModel;
    private SharedSongViewModel sharedSongViewModel;
    private ViewManagerModel viewManagerModel;

    private final JButton backButton;
    private final JPanel dataPanel;


    public SharedSongView(InGroupViewModel inGroupViewModel,
                          SharedSongViewModel sharedSongViewModel,
                          ViewManagerModel viewManagerModel) {

        this.inGroupViewModel = inGroupViewModel;
        this.sharedSongViewModel = sharedSongViewModel;
        this.viewManagerModel = viewManagerModel;

        this.sharedSongViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel("Who shares your currently playing song?");
        title.setFont(new Font("Century Schoolbook", Font.PLAIN, 25));
        title.setAlignmentX(RIGHT_ALIGNMENT);

        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        // ADD NULL CHECK:
        if (sharedSongViewModel.getState().getUsernameToShared() != null) {
            setDataPanel(sharedSongViewModel.getState().getUsernameToShared());
        }
        JPanel button = new JPanel();
        backButton = new JButton("Back to Group");
        backButton.setFont(new Font("Century Schoolbook", Font.PLAIN, 15));
        backButton.addActionListener(this);
        button.add(backButton);

        this.setLayout(new BorderLayout());
        this.add(title, BorderLayout.NORTH);
        this.add(dataPanel, BorderLayout.CENTER);
        this.add(button, BorderLayout.SOUTH);

    }

    // METHODS ARE AT CLASS LEVEL - not inside constructor!
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            viewManagerModel.setState(inGroupViewModel.getViewName());
            viewManagerModel.firePropertyChange();
        }
    }
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("shared song")) {
            setDataPanel(sharedSongViewModel.getState().getUsernameToShared());

        } else if (evt.getPropertyName().equals("error")) {
            inGroupViewModel.firePropertyChange("shared song error");
        }
    }

    public void setDataPanel(Map<String, String> UsernameToShared) {
        dataPanel.removeAll();
        dataPanel.add(Box.createVerticalStrut(10));
        if (UsernameToShared != null) {
            for (String username : UsernameToShared.keySet()) {
                JLabel label = new JLabel(username + ": " + UsernameToShared.get(username));
                label.setFont(new Font("Century Schoolbook", Font.PLAIN, 18));
                label.setAlignmentX(CENTER_ALIGNMENT);
                dataPanel.add(label);
                dataPanel.add(Box.createVerticalStrut(45));
            }
        }

        dataPanel.revalidate();
        dataPanel.repaint();
    }


    public String getViewName() { return this.viewName; }
}
