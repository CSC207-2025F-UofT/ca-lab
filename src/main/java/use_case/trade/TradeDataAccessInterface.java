package use_case.trade;

/**
 * Data access interface for trade feed operations.
 * Infrastructure implementations (e.g., Finnhub WebSocket) should implement this.
 */
public interface TradeDataAccessInterface {
    
    /**
     * Connects to the trade feed and begins streaming trades for the given symbol.
     * @param symbol The trading symbol to subscribe to (e.g., "BINANCE:BTCUSDT", "AAPL")
     * @param listener The listener to receive trade updates
     */
    void connect(String symbol, TradeListener listener);
    
    /**
     * Gracefully disconnects from the trade feed.
     */
    void disconnect();
}

