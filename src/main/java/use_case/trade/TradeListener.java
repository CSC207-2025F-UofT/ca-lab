package use_case.trade;

import entity.Trade;

/**
 * Callback interface that presentation layer (e.g., Swing UI) implements
 * to receive trade updates and connection status from a TradeFeed.
 */
public interface TradeListener {

    /**
     * Called when a new trade is received from the data source.
     */
    void onTrade(Trade trade);

    /**
     * Called when the underlying connection status changes (connecting, connected, failed, etc.).
     */
    void onStatusChanged(String statusText, boolean isError);
}

