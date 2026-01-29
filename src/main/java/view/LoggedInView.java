package view;

import interface_adapter.account.AccountController;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.stock_search.StockSearchController;
import interface_adapter.stock_search.StockSearchViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.news.NewsController;
import interface_adapter.ViewManagerModel;
import interface_adapter.market_status.MarketStatusViewModel;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final JLabel passwordErrorField = new JLabel();
    private ChangePasswordController changePasswordController = null;
    private LogoutController logoutController;
    private NewsController newsController;
    private AccountController accountController;
    private ViewManagerModel viewManagerModel;
    private String newsViewName;
    private String filterSearchViewName;
    private String historyViewName;
    private String accountViewName;
    private String realtimeTradeViewName;
    private JLabel marketStatusLabel;
    private MarketStatusViewModel marketStatusViewModel;

    private StockSearchController stockSearchController;
    private StockSearchViewModel stockSearchViewModel;

    private final JLabel username;

    private final JButton logOut;

    private final JTextField passwordInputField = new JTextField(15);
    private final JButton changePassword;

    private final JTextField searchStockField = new JTextField(25);
    private final JButton searchButton = new JButton("Search");
    private final JTextArea stockInfoArea = new JTextArea();
    private final JButton addToWatchlistButton = new JButton("Add to Watchlist");

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        username = new JLabel();

        logOut = new JButton("Log Out");
        changePassword = new JButton("Change Password");

        logOut.addActionListener(this);

        this.setLayout(new BorderLayout());

        final JPanel topToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        final JButton newsButton = new JButton("News");
        newsButton.addActionListener(e -> {
            System.out.println("News button clicked"); // debug
            if (viewManagerModel != null && newsViewName != null) {
                viewManagerModel.setState(newsViewName);
                viewManagerModel.firePropertyChange();
            }
        });

        final JButton filterSearchButton = new JButton("Filter Search");
        filterSearchButton.addActionListener(e -> {
            System.out.println("Filter Search button clicked"); // debug
            if (viewManagerModel != null && newsViewName != null) {
                viewManagerModel.setState(filterSearchViewName);
                viewManagerModel.firePropertyChange();
            }
        });


        final JButton historyButton = new JButton("History");
        historyButton.addActionListener(e -> {
            if (viewManagerModel != null && historyViewName != null) {
                viewManagerModel.setState(historyViewName);
                viewManagerModel.firePropertyChange();
            }
        });

        final JButton realtimeTradeButton = new JButton("Realtime Trade");
        realtimeTradeButton.addActionListener(e -> {
            System.out.println("Realtime Trade button clicked"); // debug
            System.out.println("viewManagerModel: " + viewManagerModel); // debug
            System.out.println("realtimeTradeViewName: " + realtimeTradeViewName); // debug
            if (viewManagerModel != null && realtimeTradeViewName != null) {
                viewManagerModel.setState(realtimeTradeViewName);
                viewManagerModel.firePropertyChange();
            } else {
                System.out.println("ERROR: Cannot navigate - viewManagerModel or realtimeTradeViewName is null"); // debug
            }
        });

        final JButton accountButton = new JButton("Account");
        accountButton.addActionListener(e -> {
            System.out.println("Account button clicked");
            if (viewManagerModel != null && accountViewName != null) {
                viewManagerModel.setState(accountViewName);
                viewManagerModel.firePropertyChange();
            }
        });

        topToolbar.add(newsButton);
        topToolbar.add(filterSearchButton);
        topToolbar.add(historyButton);
        topToolbar.add(realtimeTradeButton);
        topToolbar.add(accountButton);

        marketStatusLabel = new JLabel("Loading market status...");
        marketStatusLabel.setFont(marketStatusLabel.getFont().deriveFont(Font.PLAIN, 11f));
        marketStatusLabel.setForeground(Color.DARK_GRAY);

        topToolbar.add(Box.createHorizontalStrut(20));
        topToolbar.add(marketStatusLabel);
        this.add(topToolbar, BorderLayout.NORTH);

        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        final JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        final JLabel searchLabel = new JLabel("Search Stock:");
        searchPanel.add(searchLabel);
        searchPanel.add(searchStockField);
        searchPanel.add(searchButton);

        centerPanel.add(searchPanel);

        stockInfoArea.setLineWrap(true);
        stockInfoArea.setWrapStyleWord(true);

        final JPanel stockInfoPanel = new JPanel(new BorderLayout());
        stockInfoPanel.setBorder(BorderFactory.createTitledBorder("Stock Information"));
        stockInfoPanel.add(new JScrollPane(stockInfoArea), BorderLayout.CENTER);

        centerPanel.add(stockInfoPanel);

        this.add(centerPanel, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            if (stockSearchController != null) {
                stockSearchController.search(searchStockField.getText());
            }
        });

        final JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(addToWatchlistButton, BorderLayout.CENTER);

        addToWatchlistButton.addActionListener(e -> {
            String username = loggedInViewModel.getState().getUsername();
            System.out.println("watchlist button clicked");
            if (accountController != null) {
                JSONObject newItem = new JSONObject();
                newItem.put(searchStockField.getText().toUpperCase(), stockInfoArea.getText());
                accountController.addToWatchlist(username, newItem);
            }
        });

        this.add(bottomPanel, BorderLayout.SOUTH);

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoggedInState currentState = loggedInViewModel.getState();
                currentState.setPassword(passwordInputField.getText());
                loggedInViewModel.setState(currentState);
            }



            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        changePassword.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                evt -> {
                    if (evt.getSource().equals(changePassword)) {
                        final LoggedInState currentState = loggedInViewModel.getState();

                        this.changePasswordController.execute(
                                currentState.getUsername(),
                                currentState.getPassword()
                        );
                    }
                }
        );

    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        // TODO: execute the logout use case through the Controller
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            username.setText(state.getUsername());
        }
        else if (evt.getPropertyName().equals("password")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            if (state.getPasswordError() == null) {
                JOptionPane.showMessageDialog(this, "password updated for " + state.getUsername());
                passwordInputField.setText("");
            }
            else {
                JOptionPane.showMessageDialog(this, state.getPasswordError());
            }
        }

    }

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setStockSearchController(StockSearchController stockSearchController) {
        this.stockSearchController = stockSearchController;
    }

    public void setStockSearchViewModel(StockSearchViewModel stockSearchViewModel) {
        this.stockSearchViewModel = stockSearchViewModel;
        this.stockSearchViewModel.addPropertyChangeListener(evt -> {
            if (!"state".equals(evt.getPropertyName())) {
                return;
            }
            StockSearchViewModel vm = (StockSearchViewModel) evt.getNewValue();
            if (vm.getErrorMessage() != null && !vm.getErrorMessage().isBlank()) {
                stockInfoArea.setText(vm.getErrorMessage());
            } else if (vm.getInfoText() != null) {
                stockInfoArea.setText(vm.getInfoText());
            } else {
                stockInfoArea.setText("");
            }
        });
    }

    public void setAccountController(AccountController accountController) {
        this.accountController = accountController;
    }

    public void setLogoutController(LogoutController logoutController) {
        // TODO: save the logout controller in the instance variable.
    }

    public void setNewsNavigation(ViewManagerModel viewManagerModel, String newsViewName) {
        this.viewManagerModel = viewManagerModel;
        this.newsViewName = newsViewName;
    }

    public void setFilterSearchNavigation(ViewManagerModel viewManagerModel, String filterSearchViewName) {
        this.viewManagerModel = viewManagerModel;
        this.filterSearchViewName = filterSearchViewName;
    }

    public void setHistoryNavigation(ViewManagerModel viewManagerModel,
                                     String historyViewName) {
        this.viewManagerModel = viewManagerModel;
        this.historyViewName = historyViewName;
    }



    public void setAccountNavigation(ViewManagerModel viewManagerModel,
                                     String accountViewName) {
        this.viewManagerModel = viewManagerModel;
        this.accountViewName = accountViewName;
    }

    public void setRealtimeTradeNavigation(ViewManagerModel viewManagerModel,
                                           String tradeViewName) {
        this.viewManagerModel = viewManagerModel;
        this.realtimeTradeViewName = tradeViewName;
    }

    public void setMarketStatusViewModel(MarketStatusViewModel viewModel) {
        this.marketStatusViewModel = viewModel;

        // Listen for changes in market status
        this.marketStatusViewModel.addPropertyChangeListener(evt -> {
            if (!"state".equals(evt.getPropertyName())) return;
            updateMarketStatusLabel();
        });
    }

    private void updateMarketStatusLabel() {
        if (marketStatusViewModel == null) {
            return;
        }

        String text = marketStatusViewModel.getStatusText();
        if (text == null || text.isBlank()) {
            marketStatusLabel.setText("Market status unavailable");
            marketStatusLabel.setForeground(Color.GRAY);
            return;
        }

        marketStatusLabel.setText(text);

        if (marketStatusViewModel.getErrorMessage() != null) {
            marketStatusLabel.setForeground(Color.RED);
        } else if (marketStatusViewModel.isOpen()) {
            marketStatusLabel.setForeground(new Color(0, 128, 0)); // green-ish
        } else {
            marketStatusLabel.setForeground(Color.GRAY);
        }
    }
}
