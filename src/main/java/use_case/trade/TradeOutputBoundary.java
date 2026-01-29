package use_case.trade;

/**
 * Output boundary for the trade use case.
 */
public interface TradeOutputBoundary {
    /**
     * Prepares the success view when a trade is received.
     * @param responseModel The trade response model
     */
    void prepareSuccessView(TradeResponseModel responseModel);
    
    /**
     * Prepares the fail view when an error occurs.
     * @param errorMessage The error message
     */
    void prepareFailView(String errorMessage);
    
    /**
     * Prepares the status update view.
     * @param statusText The status text
     * @param isError Whether this is an error status
     */
    void prepareStatusView(String statusText, boolean isError);
}

