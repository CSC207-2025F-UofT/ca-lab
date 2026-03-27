package use_case.trade;

import entity.Trade;

/**
 * Response model for trade updates.
 */
public class TradeResponseModel {
    private final Trade trade;
    private final String statusText;
    private final boolean isError;
    
    public TradeResponseModel(Trade trade, String statusText, boolean isError) {
        this.trade = trade;
        this.statusText = statusText;
        this.isError = isError;
    }
    
    public Trade getTrade() {
        return trade;
    }
    
    public String getStatusText() {
        return statusText;
    }
    
    public boolean isError() {
        return isError;
    }
}

