package use_case.stock_search;

public interface StockSearchOutputBoundary {
    void prepareSuccessView(StockSearchResponseModel responseModel);
    void prepareFailView(String errorMessage);
}
