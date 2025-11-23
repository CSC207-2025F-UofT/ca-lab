package view;

import interface_adapter.manage_application.ManageApplicationViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class ManageApplicationView extends JPanel implements PropertyChangeListener {

    private final String viewName = "applications";
    private final ManageApplicationViewModel viewModel;

    // Container that holds all the cards
    private final JPanel cardsPanel = new JPanel();

    public ManageApplicationView(ManageApplicationViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Applications");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Configure card list panel
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(Color.WHITE);

        // Scroll pane containing the cards
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        // TEMP: Add 3 example empty cards to show the layout working
        addPlaceholderCards();
    }

    // Add example empty cards so the UI renders before data exists.
    private void addPlaceholderCards() {
        for (int i = 0; i < 3; i++) {
            cardsPanel.add(createApplicationCard("Application " + (i + 1)));
        }
    }

    //Creates a single card panel representing one application.
    private JPanel createApplicationCard(String titleText) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.GRAY, 1)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // full width
        card.setBackground(Color.WHITE);

        JLabel title = new JLabel(titleText);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

        JLabel details = new JLabel("Details go here...");
        details.setFont(details.getFont().deriveFont(12f));

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(details);

        return card;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // When ViewModel has new list → rebuild cards
        // Eventually: render real application data
    }

    public String getViewName() {
        return viewName;
    }
}


