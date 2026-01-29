package interface_adapter.trade;

import entity.Trade;

/**
 * State for the Trade view.
 */
public class TradeState {
    private Trade currentTrade;
    private String statusText = "Status: Disconnected";
    private boolean isError = false;
    private String symbol = "";
    
    public TradeState() {
    }
    
    public Trade getCurrentTrade() {
        return currentTrade;
    }
    
    public void setCurrentTrade(Trade currentTrade) {
        this.currentTrade = currentTrade;
    }
    
    public String getStatusText() {
        return statusText;
    }
    
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
    
    public boolean isError() {
        return isError;
    }
    
    public void setError(boolean error) {
        isError = error;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

