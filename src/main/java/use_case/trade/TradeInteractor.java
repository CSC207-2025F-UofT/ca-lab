package use_case.trade;

import entity.Trade;

/**
 * Interactor for the trade use case.
 * Implements the business logic for connecting to and receiving trade data.
 */
public class TradeInteractor implements TradeInputBoundary {
    
    private final TradeDataAccessInterface tradeDataAccess;
    private final TradeOutputBoundary tradePresenter;
    private final TradeListener internalListener;
    
    public TradeInteractor(TradeDataAccessInterface tradeDataAccess,
                          TradeOutputBoundary tradePresenter) {
        this.tradeDataAccess = tradeDataAccess;
        this.tradePresenter = tradePresenter;
        
        // Create internal listener that converts TradeListener callbacks to OutputBoundary calls
        this.internalListener = new TradeListener() {
            @Override
            public void onTrade(Trade trade) {
                TradeResponseModel responseModel = new TradeResponseModel(trade, null, false);
                tradePresenter.prepareSuccessView(responseModel);
            }
            
            @Override
            public void onStatusChanged(String statusText, boolean isError) {
                tradePresenter.prepareStatusView(statusText, isError);
            }
        };
    }
    
    @Override
    public void execute(TradeRequestModel requestModel) {
        String symbol = requestModel.getSymbol();
        
        if (symbol == null || symbol.trim().isEmpty()) {
            tradePresenter.prepareFailView("Please enter a symbol.");
            return;
        }
        
        symbol = symbol.trim();
        
        try {
            tradeDataAccess.connect(symbol, internalListener);
        } catch (Exception e) {
            tradePresenter.prepareFailView("Unable to connect to trade feed. " + e.getMessage());
        }
    }
    
    @Override
    public void disconnect() {
        try {
            tradeDataAccess.disconnect();
        } catch (Exception e) {
            tradePresenter.prepareFailView("Unable to disconnect. " + e.getMessage());
        }
    }
}

