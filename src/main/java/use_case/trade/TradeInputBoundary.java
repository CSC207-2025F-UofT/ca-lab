package use_case.trade;

/**
 * Input boundary for the trade use case.
 */
public interface TradeInputBoundary {
    /**
     * Executes the trade connection use case.
     * @param requestModel The request containing the symbol to connect to
     */
    void execute(TradeRequestModel requestModel);
    
    /**
     * Executes the trade disconnection use case.
     */
    void disconnect();
}

