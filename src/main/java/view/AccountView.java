package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.account.AccountController;
import interface_adapter.account.AccountViewModel;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class AccountView extends JPanel implements PropertyChangeListener {
    private AccountController accountController;
    private ViewManagerModel viewManagerModel;
    private String homeViewName;
    private final String viewName = "account";
    private final AccountViewModel viewModel;
    private final JPanel watchlistPanel;
    private final JScrollPane scrollPane;
    private final JLabel usernameLabel;
    private final JButton backButton;

    public AccountView(AccountViewModel accountViewModel) {
        this.viewModel = accountViewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        backButton = new JButton("Back");
        topPanel.add(backButton, BorderLayout.WEST);

        backButton.addActionListener(e -> {
            if (viewManagerModel != null && homeViewName != null) {
                viewManagerModel.setState(homeViewName);
                viewManagerModel.firePropertyChange();
            }
        });

        usernameLabel = new JLabel("Watchlist");
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(usernameLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        watchlistPanel = new JPanel();
        watchlistPanel.setLayout(new BoxLayout(watchlistPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(watchlistPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void propertyChange(PropertyChangeEvent evt) {

        watchlistPanel.removeAll();

        ArrayList<JSONObject> watchlist = viewModel.getState().getWatchlist();
        if (watchlist == null) {
            watchlist = new ArrayList<>();
        }

        for (JSONObject item : watchlist) {
            if (item == null) continue;
            final JSONObject itemToRemove = item;

            String text = null;
            for (String key : item.keySet()) {
                if (item.get(key) instanceof String) {
                    String value = item.getString(key).trim();
                    if (!value.isEmpty()) {
                        text = value;
                        break;
                    }
                }
            }

            if (text != null && !text.isEmpty()) {
                JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JLabel stockLabel = new JLabel(text);
                itemPanel.add(stockLabel);

                JButton removeButton = new JButton("Remove");

                removeButton.addActionListener(e -> {
                    String username = viewModel.getState().getUsername();
                    System.out.println(username);
                    accountController.removeFromWatchlist(username, itemToRemove);
                });

                itemPanel.add(removeButton);

                watchlistPanel.add(itemPanel);
            }
        }
        watchlistPanel.revalidate();
        watchlistPanel.repaint();
    }

    public String getViewName() {
        return viewName;
    }

    public void loadAccount(String username) {
        if (accountController != null) {
            accountController.loadWatchlist(username);
        }
    }

    public void setBackNavigation(ViewManagerModel viewManagerModel, String homeViewName) {
        this.viewManagerModel = viewManagerModel;
        this.homeViewName = homeViewName;
    }

    public void setController(AccountController accountController) {
        this.accountController = accountController;
        }

}
