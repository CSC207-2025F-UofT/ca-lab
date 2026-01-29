package use_case.market_status;

public interface MarketStatusOutputBoundary {

    void prepareSuccessView(MarketStatusResponseModel responseModel);

    void prepareFailView(String errorMessage);
}