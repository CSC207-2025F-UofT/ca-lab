package view;

import entity.Trade;
import interface_adapter.ViewManagerModel;
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
    private static final long CONNECTION_COOLDOWN_MS = 5000; // 5 seconds between connection attempts

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
        symbolInputField.setText("BINANCE:BTCUSDT"); // Default value
        symbolInputField.setToolTipText("Enter crypto pair (e.g., BINANCE:BTCUSDT). Stock symbols may not have real-time trade data.");
        searchPanel.add(symbolInputField);
        
        // Add a help label to guide users
        JLabel helpLabel = new JLabel("<html><small style='color:gray;'>Note: Crypto pairs work best (e.g., BINANCE:BTCUSDT)</small></html>");
        searchPanel.add(helpLabel);
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
     * Handles the Connect button click by delegating to the Controller.
     */
    private void onConnectClicked() {
        String symbol = symbolInputField.getText().trim();
        if (symbol.isEmpty()) {
            statusLabel.setText("Status: Please enter a symbol");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        // Warn user if they're using a stock symbol (not crypto)
        if (!symbol.contains(":") || !symbol.toUpperCase().startsWith("BINANCE:")) {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Stock symbols (like AAPL, TSLA) may not have real-time trade data available.\n\n" +
                "Crypto pairs (like BINANCE:BTCUSDT) work best for real-time trades.\n\n" +
                "Do you want to continue with '" + symbol + "'?",
                "Symbol Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
        }
        
        // Check rate limiting - prevent too frequent connection attempts
        long currentTime = System.currentTimeMillis();
        long timeSinceLastAttempt = currentTime - lastConnectionAttemptTime;
        
        if (timeSinceLastAttempt < CONNECTION_COOLDOWN_MS) {
            long remainingTime = (CONNECTION_COOLDOWN_MS - timeSinceLastAttempt) / 1000;
            JOptionPane.showMessageDialog(
                this,
                "Please wait " + remainingTime + " second(s) before connecting again.\nToo many requests may result in rate limiting.",
                "Rate Limit",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Always disconnect first to ensure clean state
        if (isConnected || connectionStartTime > 0) {
            tradeController.disconnect();
            // Reset state immediately
            isConnected = false;
            connectionStartTime = 0;
            // Give a delay for disconnect to complete
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Update last connection attempt time
        lastConnectionAttemptTime = currentTime;
        
        // Reset state and set new connection start time
        currentSymbol = symbol;
        connectionStartTime = System.currentTimeMillis();
        symbolLabel.setText("---");
        priceLabel.setText("---");
        volumeLabel.setText("---");
        timestampLabel.setText("---");
        
        // Disable connect button during connection attempt, enable disconnect button
        connectButton.setEnabled(false);
        disconnectButton.setEnabled(true);
        
        // Immediately show "Connecting..." while we wait for the first trade.
        statusLabel.setText("Status: Connecting...");
        statusLabel.setForeground(Color.ORANGE);
        
        // Execute through controller
        tradeController.execute(symbol);
        
        // Start a timeout check for symbol not found
        startSymbolNotFoundCheck();
    }
    
    /**
     * Handles the Disconnect button click.
     */
    private void onDisconnectClicked() {
        // Disconnect through controller
        tradeController.disconnect();
        
        // Reset all info to default
        resetToDefault();
    }
    
    /**
     * Resets all information to default values.
     */
    private void resetToDefault() {
        // Reset connection state
        isConnected = false;
        connectionStartTime = 0;
        currentSymbol = "";
        
        // Reset UI labels to default
        symbolLabel.setText("---");
        priceLabel.setText("---");
        volumeLabel.setText("---");
        timestampLabel.setText("---");
        statusLabel.setText("Status: Disconnected");
        statusLabel.setForeground(Color.BLACK);
        
        // Reset button states
        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
        connectButton.setText("Connect");
    }
    
    /**
     * Starts a background thread to check if symbol is not found after timeout.
     */
    private void startSymbolNotFoundCheck() {
        // Capture the connection start time and symbol for this specific connection attempt
        final long checkStartTime = connectionStartTime;
        final String checkSymbol = currentSymbol;
        
        new Thread(() -> {
            try {
                Thread.sleep(SYMBOL_NOT_FOUND_TIMEOUT);
                SwingUtilities.invokeLater(() -> {
                    // Only trigger timeout if:
                    // 1. We're still checking the same symbol
                    // 2. The connection start time hasn't changed (no new connection started)
                    // 3. Enough time has passed since connection started
                    // 4. No trades have been received (symbol label still shows "---")
                    long currentTime = System.currentTimeMillis();
                    if (checkSymbol.equals(currentSymbol) && 
                        connectionStartTime == checkStartTime &&
                        connectionStartTime > 0 &&
                        currentTime - connectionStartTime >= SYMBOL_NOT_FOUND_TIMEOUT &&
                        symbolLabel.getText().equals("---")) {
                            // No trades received - could be symbol not found or rate limiting
                            boolean isCrypto = currentSymbol.contains(":") && currentSymbol.toUpperCase().startsWith("BINANCE:");
                            String message = "Connection timeout: Symbol '" + currentSymbol + "' not found or no trades available.\n\n";
                            if (!isCrypto) {
                                message += "⚠️ IMPORTANT: Stock symbols (like AAPL, TSLA) typically do NOT have real-time trade data on Finnhub.\n" +
                                          "The WebSocket trade feed primarily supports CRYPTO pairs.\n\n";
                            }
                            message += "This may also occur if:\n" +
                                      "• Requests are made too frequently (rate limiting)\n" +
                                      "• The symbol format is incorrect\n" +
                                      "• The market is closed\n\n" +
                                      "✅ RECOMMENDED: Use crypto pairs like:\n" +
                                      "   • BINANCE:BTCUSDT\n" +
                                      "   • BINANCE:ETHUSDT\n" +
                                      "   • BINANCE:BNBUSDT\n\n" +
                                      "Please wait " + (CONNECTION_COOLDOWN_MS / 1000) + 
                                      " seconds before trying again.";
                            
                            JOptionPane.showMessageDialog(
                                TradeView.this,
                                message,
                                "Connection Error",
                                JOptionPane.ERROR_MESSAGE
                            );
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
                for (int remaining = (int)(CONNECTION_COOLDOWN_MS / 1000); remaining > 0; remaining--) {
                    final int seconds = remaining;
                    SwingUtilities.invokeLater(() -> {
                        connectButton.setText("Wait " + seconds + "s");
                        statusLabel.setText("Status: Rate limited - wait " + seconds + " second(s)");
                        statusLabel.setForeground(Color.ORANGE);
                    });
                    Thread.sleep(1000);
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
                boolean isRateLimit = statusText.contains("429") || statusText.contains("Too Many Requests") || statusText.contains("Rate limit");
                boolean isOtherError = statusText.contains("Error") || statusText.contains("Failure");
                
                if (isRateLimit || isOtherError) {
                    String message;
                    if (isRateLimit) {
                        message = "Connection failed: Too Many Requests (429).\n" +
                                  "This may also occur if the symbol '" + currentSymbol + "' is invalid.\n\n" +
                                  "Please wait " + (CONNECTION_COOLDOWN_MS / 1000) + 
                                  " seconds before trying again.\n" +
                                  "If the problem persists, please verify the symbol is correct.";
                        // Enforce cooldown after 429 error
                        lastConnectionAttemptTime = System.currentTimeMillis();
                        startCooldownTimer();
                    } else {
                        message = "Connection failed: Symbol '" + currentSymbol + "' not found or invalid.\n" +
                                  "This may also occur if requests are made too frequently.\n\n" +
                                  "Please wait " + (CONNECTION_COOLDOWN_MS / 1000) + 
                                  " seconds before trying again.\n" +
                                  "If the problem persists, please verify the symbol is correct.";
                        // Enforce cooldown for other errors too
                        lastConnectionAttemptTime = System.currentTimeMillis();
                        startCooldownTimer();
                    }
                    
                    JOptionPane.showMessageDialog(
                        TradeView.this,
                        message,
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else if (statusText != null && statusText.contains("Connected")) {
                statusLabel.setForeground(new Color(0, 128, 0));
            } else if (statusText != null && statusText.contains("Connecting")) {
                statusLabel.setForeground(Color.ORANGE);
            } else if (statusText != null && statusText.contains("Disconnected")) {
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
}
