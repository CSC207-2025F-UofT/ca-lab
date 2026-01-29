package view;

import entity.Trade;
import interface_adapter.ViewManagerModel;
import interface_adapter.market_status.MarketStatusViewModel;
import interface_adapter.trade.TradeController;
import interface_adapter.trade.TradeState;
import interface_adapter.trade.TradeViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Swing view for displaying real-time trade data.
 * Follows Clean Architecture pattern using Controller and ViewModel.
 */
public class TradeView extends JPanel implements PropertyChangeListener {

    private final String viewName = "trade";
    private ViewManagerModel viewManagerModel;
    private String homeViewName;

    private final TradeController tradeController;
    private final TradeViewModel tradeViewModel;
    private MarketStatusViewModel marketStatusViewModel;

    // Trade data labels
    private final JLabel symbolLabel = new JLabel("---");
    private final JLabel priceLabel = new JLabel("---");
    private final JLabel volumeLabel = new JLabel("---");
    private final JLabel timestampLabel = new JLabel("---");
    private final JLabel statusLabel = new JLabel("Status: Disconnected", SwingConstants.CENTER);
    private final JTextField symbolInputField = new JTextField(20);
    private JButton connectButton;
    private JButton disconnectButton;
    private String currentSymbol = "";
    private long connectionStartTime = 0;
    private long lastConnectionAttemptTime = 0;
    private boolean isConnected = false;
    private static final long SYMBOL_NOT_FOUND_TIMEOUT = 10000; // 10 seconds
    private static final long CONNECTION_COOLDOWN_MS = 5000; // 5 seconds
    private static final int DISCONNECT_DELAY_MS = 300;
    private static final int ONE_SECOND_MS = 1000;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final String STATUS_CONNECTED = "Connected";
    private static final String STATUS_CONNECTING = "Connecting";
    private static final String STATUS_DISCONNECTED = "Disconnected";
    private static final String CRYPTO_PAIR_EXAMPLE = "BINANCE:BTCUSDT";

    /**
     * Initializes the GUI components.
     */
    public TradeView(TradeController tradeController, TradeViewModel tradeViewModel) {
        this.tradeController = tradeController;
        this.tradeViewModel = tradeViewModel;
        
        tradeViewModel.addPropertyChangeListener(this);
        
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> {
            if (viewManagerModel != null && homeViewName != null) {
                viewManagerModel.setState(homeViewName);
                viewManagerModel.firePropertyChange();
            }
        });
        backPanel.add(backButton);
        topPanel.add(backPanel, BorderLayout.WEST);

        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        topPanel.add(statusLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // --- Center: Trade data display ---
        JPanel mainPanel = createMainDashboardCard();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the main dashboard card that shows the trade data and connect button.
     */
    private JPanel createMainDashboardCard() {
        JPanel root = new JPanel(new BorderLayout(10, 10));

        // Search panel at top with Connect and Disconnect buttons
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.add(new JLabel("Symbol:"));
        symbolInputField.setText(CRYPTO_PAIR_EXAMPLE);
        symbolInputField.setToolTipText("Enter crypto pair (e.g., "
                + CRYPTO_PAIR_EXAMPLE + ") or stock symbol (e.g., AAPL)");
        searchPanel.add(symbolInputField);
        connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> onConnectClicked());
        searchPanel.add(connectButton);
        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(e -> onDisconnectClicked());
        disconnectButton.setEnabled(false); // Initially disabled
        searchPanel.add(disconnectButton);
        root.add(searchPanel, BorderLayout.NORTH);

        // Data display panel
        JPanel dataPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        styleLabel(symbolLabel);
        styleLabel(priceLabel);
        styleLabel(volumeLabel);
        styleLabel(timestampLabel);

        dataPanel.add(new JLabel("Symbol:"));
        dataPanel.add(symbolLabel);

        dataPanel.add(new JLabel("Last Price (P):"));
        dataPanel.add(priceLabel);

        dataPanel.add(new JLabel("Volume (V):"));
        dataPanel.add(volumeLabel);

        dataPanel.add(new JLabel("Timestamp (T):"));
        dataPanel.add(timestampLabel);

        root.add(dataPanel, BorderLayout.CENTER);

        return root;
    }

    /**
     * Helper to apply basic styling to the data display labels.
     */
    private void styleLabel(JLabel label) {
        label.setFont(new Font("Monospaced", Font.BOLD, 18));
        label.setForeground(new Color(0, 128, 0)); // Green color for data
    }
    
    /**
     * Determines if a symbol is a cryptocurrency pair or a stock symbol.
     * @param symbol The symbol to check
     * @return "Crypto" if it's a crypto pair, "Stock" if it's a stock, "Unknown" if unclear
     */
    private String detectSymbolType(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            return "Unknown";
        }
        
        String upperSymbol = symbol.trim().toUpperCase();
        
        // Check for crypto pair format: EXCHANGE:PAIR (e.g., BINANCE:BTCUSDT, COINBASE:BTCUSD)
        if (upperSymbol.contains(":")) {
            String[] parts = upperSymbol.split(":", 2);
            if (parts.length == 2) {
                String exchange = parts[0];
                String pair = parts[1];
                
                // Common crypto exchanges
                if (exchange.equals("BINANCE") || exchange.equals("COINBASE")
                        || exchange.equals("KRAKEN") || exchange.equals("BITSTAMP")
                        || exchange.equals("BITFINEX") || exchange.equals("GEMINI")) {
                    // Check if pair looks like a crypto pair
                    // (usually ends with USD, USDT, EUR, etc.)
                    if (pair.endsWith("USD") || pair.endsWith("USDT")
                            || pair.endsWith("EUR") || pair.endsWith("BTC")
                            || pair.endsWith("ETH") || pair.length() >= 6) {
                        return "Crypto";
                    }
                }
            }
        }
        
        // Stock symbols are typically 1-5 uppercase letters/numbers, no special characters
        // Common patterns: AAPL, TSLA, GOOGL, BRK.B, 005930 (Korean stocks)
        if (upperSymbol.matches("^[A-Z0-9]{1,5}$") || upperSymbol.matches("^[A-Z0-9]+\\.[A-Z]$")) {
            return "Stock";
        }
        
        return "Unknown";
    }
    
    /**
     * Handles the Connect button click by delegating to the Controller.
     */
    private void onConnectClicked() {
        String symbol = symbolInputField.getText().trim();
        if (symbol.isEmpty()) {
            statusLabel.setText("Status: Please enter a symbol");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        // Detect symbol type
        String symbolType = detectSymbolType(symbol);
        
        // If it's a stock symbol, check if market is closed
        if (symbolType.equals("Stock")) {
            if (marketStatusViewModel != null && !marketStatusViewModel.isOpen()) {
                String marketStatusText = marketStatusViewModel.getStatusText();
                if (marketStatusText == null) {
                    marketStatusText = "Market is closed";
                }
                String message = "Cannot connect to stock symbol '"
                        + symbol + "' because the market is closed.\n\n"
                        + "Market Status: " + marketStatusText + "\n\n"
                        + "Please try:\n"
                        + "• Wait until the market opens\n"
                        + "• Use a cryptocurrency pair instead (e.g., "
                        + CRYPTO_PAIR_EXAMPLE + ")";
                JOptionPane.showMessageDialog(
                        this,
                        message,
                        "Market Closed",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Warn user if they're using a stock symbol (not crypto)
            String warningMessage = "Stock symbols (like AAPL, TSLA) "
                    + "may not have real-time trade data available.\n\n"
                    + "Do you want to continue with '" + symbol + "'?";
            int result = JOptionPane.showConfirmDialog(
                    this,
                    warningMessage,
                    "Symbol Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
        }
        
        // Check rate limiting - prevent too frequent connection attempts
        long currentTime = System.currentTimeMillis();
        long timeSinceLastAttempt = currentTime - lastConnectionAttemptTime;
        
        if (timeSinceLastAttempt < CONNECTION_COOLDOWN_MS) {
            long remainingTime = (CONNECTION_COOLDOWN_MS - timeSinceLastAttempt)
                    / MILLISECONDS_PER_SECOND;
            String rateLimitMessage = "Please wait " + remainingTime
                    + " second(s) before connecting again.\n"
                    + "Too many requests may result in rate limiting.";
            JOptionPane.showMessageDialog(
                    this,
                    rateLimitMessage,
                    "Rate Limit",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (isConnected || connectionStartTime > 0) {
            tradeController.disconnect();
            isConnected = false;
            connectionStartTime = 0;
            try {
                Thread.sleep(DISCONNECT_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        lastConnectionAttemptTime = currentTime;

        currentSymbol = symbol;
        connectionStartTime = System.currentTimeMillis();
        symbolLabel.setText("---");
        priceLabel.setText("---");
        volumeLabel.setText("---");
        timestampLabel.setText("---");

        connectButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        statusLabel.setText("Status: Connecting...");
        statusLabel.setForeground(Color.ORANGE);

        tradeController.execute(symbol);

        // Start a timeout check for symbol not found
        startSymbolNotFoundCheck();
    }
    
    /**
     * Handles the Disconnect button click.
     */
    private void onDisconnectClicked() {
        tradeController.disconnect();
        resetToDefault();
    }
    
    /**
     * Resets all information to default values.
     */
    private void resetToDefault() {
        isConnected = false;
        connectionStartTime = 0;
        currentSymbol = "";

        symbolLabel.setText("---");
        priceLabel.setText("---");
        volumeLabel.setText("---");
        timestampLabel.setText("---");
        statusLabel.setText("Status: Disconnected");
        statusLabel.setForeground(Color.BLACK);

        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
        connectButton.setText("Connect");
    }
    
    /**
     * Starts a background thread to check if symbol is not found after timeout.
     */
    private void startSymbolNotFoundCheck() {
        final long checkStartTime = connectionStartTime;
        final String checkSymbol = currentSymbol;
        
        new Thread(() -> {
            try {
                Thread.sleep(SYMBOL_NOT_FOUND_TIMEOUT);
                SwingUtilities.invokeLater(() -> {
                    long currentTime = System.currentTimeMillis();
                    if (checkSymbol.equals(currentSymbol)
                            && connectionStartTime == checkStartTime
                            && connectionStartTime > 0
                            && currentTime - connectionStartTime
                            >= SYMBOL_NOT_FOUND_TIMEOUT
                            && symbolLabel.getText().equals("---")) {
                            // No trades received - could be symbol not found
                            // or rate limiting
                            boolean isCrypto = currentSymbol.contains(":")
                                    && currentSymbol.toUpperCase()
                                    .startsWith("BINANCE:");
                            String message = "Connection timeout: Symbol '"
                                    + currentSymbol
                                    + "' not found or no trades available.\n\n";
                            if (!isCrypto) {
                                message += "⚠️ IMPORTANT: Stock symbols "
                                        + "(like AAPL, TSLA) typically do NOT "
                                        + "have real-time trade data on Finnhub.\n"
                                        + "The WebSocket trade feed primarily "
                                        + "supports CRYPTO pairs.\n\n";
                            }
                            message += "This may also occur if:\n"
                                    + "• Requests are made too frequently "
                                    + "(rate limiting)\n"
                                    + "• The symbol format is incorrect\n"
                                    + "• The market is closed\n\n"
                                    + "✅ RECOMMENDED: Use crypto pairs like:\n"
                                    + "   • BINANCE:BTCUSDT\n"
                                    + "   • BINANCE:ETHUSDT\n"
                                    + "   • BINANCE:BNBUSDT\n\n"
                                    + "Please wait "
                                    + (CONNECTION_COOLDOWN_MS / MILLISECONDS_PER_SECOND)
                                    + " seconds before trying again.";

                            JOptionPane.showMessageDialog(
                                    TradeView.this,
                                    message,
                                    "Connection Error",
                                    JOptionPane.ERROR_MESSAGE);
                            statusLabel.setText("Status: Connection failed");
                            statusLabel.setForeground(Color.RED);
                            tradeController.disconnect();
                            resetToDefault();
                            // Enforce cooldown after timeout
                            lastConnectionAttemptTime = System.currentTimeMillis();
                            startCooldownTimer();
                        }
                    });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * Starts a cooldown timer that disables the connect button and shows countdown.
     */
    private void startCooldownTimer() {
        connectButton.setEnabled(false);
        new Thread(() -> {
            try {
                int cooldownSeconds = (int) (CONNECTION_COOLDOWN_MS
                        / MILLISECONDS_PER_SECOND);
                for (int remaining = cooldownSeconds; remaining > 0; remaining--) {
                    final int seconds = remaining;
                    SwingUtilities.invokeLater(() -> {
                        connectButton.setText("Wait " + seconds + "s");
                        statusLabel.setText("Status: Rate limited - wait "
                                + seconds + " second(s)");
                        statusLabel.setForeground(Color.ORANGE);
                    });
                    Thread.sleep(ONE_SECOND_MS);
                }
                SwingUtilities.invokeLater(() -> {
                    connectButton.setText("Connect");
                    connectButton.setEnabled(true);
                    disconnectButton.setEnabled(false);
                    statusLabel.setText("Status: Ready to connect");
                    statusLabel.setForeground(Color.BLACK);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                SwingUtilities.invokeLater(() -> {
                    connectButton.setText("Connect");
                    connectButton.setEnabled(true);
                    disconnectButton.setEnabled(false);
                });
            }
        }).start();
    }

    /**
     * Updates UI labels when a new trade is received.
     */
    private void updateTradeOnUi(Trade trade) {
        SwingUtilities.invokeLater(() -> {
            String symbol = trade.getSymbol();
            double price = trade.getPrice();
            double volume = trade.getVolume();
            Instant timestamp = trade.getTimestamp();

            symbolLabel.setText(symbol != null ? symbol : "N/A");
            priceLabel.setText(String.format("$%,.2f", price));
            volumeLabel.setText(String.format("%,.4f", volume));

            if (timestamp != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                String time = timestamp
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime()
                        .format(formatter);
                timestampLabel.setText(time);
            } else {
                timestampLabel.setText("N/A");
            }
            
            // Mark as connected on first trade
            connectionStartTime = 0; // Reset timeout on first trade
            isConnected = true;
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
        });
    }

    /**
     * Updates the status label on the Event Dispatch Thread.
     */
    private void updateStatusOnUi(String statusText, boolean isError) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(statusText);
            if (isError) {
                statusLabel.setForeground(Color.RED);
                // On error, re-enable connect button and disable disconnect
                connectButton.setEnabled(true);
                disconnectButton.setEnabled(false);
                isConnected = false;
                
                // Combined error handling for rate limiting and symbol not found
                boolean isRateLimit = statusText.contains("429")
                        || statusText.contains("Too Many Requests")
                        || statusText.contains("Rate limit");
                boolean isOtherError = statusText.contains("Error")
                        || statusText.contains("Failure");

                if (isRateLimit || isOtherError) {
                    String message;
                    long cooldownSeconds = CONNECTION_COOLDOWN_MS
                            / MILLISECONDS_PER_SECOND;
                    if (isRateLimit) {
                        message = "Connection failed: Too Many Requests (429).\n"
                                + "This may also occur if the symbol '"
                                + currentSymbol + "' is invalid.\n\n"
                                + "Please wait " + cooldownSeconds
                                + " seconds before trying again.\n"
                                + "If the problem persists, please verify "
                                + "the symbol is correct.";
                        // Enforce cooldown after 429 error
                        lastConnectionAttemptTime = System.currentTimeMillis();
                        startCooldownTimer();
                    } else {
                        message = "Connection failed: Symbol '"
                                + currentSymbol
                                + "' not found or invalid.\n"
                                + "This may also occur if requests are made "
                                + "too frequently.\n\n"
                                + "Please wait " + cooldownSeconds
                                + " seconds before trying again.\n"
                                + "If the problem persists, please verify "
                                + "the symbol is correct.";
                        // Enforce cooldown for other errors too
                        lastConnectionAttemptTime = System.currentTimeMillis();
                        startCooldownTimer();
                    }

                    JOptionPane.showMessageDialog(
                            TradeView.this,
                            message,
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (statusText != null
                    && statusText.contains(STATUS_CONNECTED)) {
                statusLabel.setForeground(new Color(0, 128, 0));
            } else if (statusText != null
                    && statusText.contains(STATUS_CONNECTING)) {
                statusLabel.setForeground(Color.ORANGE);
            } else if (statusText != null
                    && statusText.contains(STATUS_DISCONNECTED)) {
                statusLabel.setForeground(Color.BLACK);
                // On disconnect, reset button states
                connectButton.setEnabled(true);
                disconnectButton.setEnabled(false);
                isConnected = false;
            } else {
                statusLabel.setForeground(Color.BLACK);
            }
        });
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        TradeState state = tradeViewModel.getState();
        if (state != null) {
            // Update UI based on ViewModel state
            if (state.getCurrentTrade() != null) {
                updateTradeOnUi(state.getCurrentTrade());
            }
            if (state.getStatusText() != null) {
                updateStatusOnUi(state.getStatusText(), state.isError());
            }
        }
    }

    public String getViewName() {
        return this.viewName;
    }

    public void setBackNavigation(ViewManagerModel viewManagerModel, String homeViewName) {
        this.viewManagerModel = viewManagerModel;
        this.homeViewName = homeViewName;
    }
    
    /**
     * Sets the market status view model to check if market is open/closed.
     */
    public void setMarketStatusViewModel(MarketStatusViewModel marketStatusViewModel) {
        this.marketStatusViewModel = marketStatusViewModel;
    }
}
