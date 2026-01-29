package interface_adapter.trade;

import entity.Trade;
import use_case.trade.TradeOutputBoundary;
import use_case.trade.TradeResponseModel;

/**
 * Presenter for the Trade use case.
 */
public class TradePresenter implements TradeOutputBoundary {
    
    private final TradeViewModel viewModel;
    
    public TradePresenter(TradeViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void prepareSuccessView(TradeResponseModel responseModel) {
        Trade trade = responseModel.getTrade();
        if (trade != null) {
            viewModel.setCurrentTrade(trade);
        }
    }
    
    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setStatus(errorMessage, true);
    }
    
    @Override
    public void prepareStatusView(String statusText, boolean isError) {
        viewModel.setStatus(statusText, isError);
    }
}

