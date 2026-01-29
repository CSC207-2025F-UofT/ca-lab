package interface_adapter.trade;

import interface_adapter.ViewModel;

/**
 * ViewModel for the Trade view.
 */
public class TradeViewModel extends ViewModel<TradeState> {
    
    public TradeViewModel() {
        super("trade");
        setState(new TradeState());
    }
    
    public void setCurrentTrade(entity.Trade trade) {
        TradeState state = getState();
        state.setCurrentTrade(trade);
        firePropertyChange();
    }
    
    public void setStatus(String statusText, boolean isError) {
        TradeState state = getState();
        state.setStatusText(statusText);
        state.setError(isError);
        firePropertyChange();
    }
    
    public void setSymbol(String symbol) {
        TradeState state = getState();
        state.setSymbol(symbol);
        firePropertyChange();
    }
}

