package interface_adapter.trade;

import use_case.trade.TradeInputBoundary;
import use_case.trade.TradeRequestModel;

/**
 * Controller for the Trade use case.
 */
public class TradeController {
    
    private final TradeInputBoundary tradeInteractor;
    
    public TradeController(TradeInputBoundary tradeInteractor) {
        this.tradeInteractor = tradeInteractor;
    }
    
    public void execute(String symbol) {
        TradeRequestModel requestModel = new TradeRequestModel(symbol);
        tradeInteractor.execute(requestModel);
    }
    
    public void disconnect() {
        tradeInteractor.disconnect();
    }
}

