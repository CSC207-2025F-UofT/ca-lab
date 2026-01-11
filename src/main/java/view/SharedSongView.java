package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.InGroupViewModel;
import interface_adapter.sharedsong.SharedSongViewModel;

import javax.swing.*;
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

        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        setDataPanel(sharedSongViewModel.getState().getUsernameToShared());

        backButton = new JButton("Back to Group");
        backButton.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(dataPanel);
        this.add(backButton);
    }

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

        for (String username : UsernameToShared.keySet()) {
            dataPanel.add(new JLabel(username + ": " + UsernameToShared.get(username)));
        }

        dataPanel.revalidate();
        dataPanel.repaint();
    }


    public String getViewName() { return this.viewName; }
}
