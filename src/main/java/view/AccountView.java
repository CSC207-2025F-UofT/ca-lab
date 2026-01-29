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

            String text = null;
            if (item.has("stock")) {
                text = item.getString("stock");
            } else if (item.has("info")) {
                text = item.getString("info");
            }

            if (text != null && !text.isEmpty()) {
                JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JLabel stockLabel = new JLabel(text);
                itemPanel.add(stockLabel);

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
