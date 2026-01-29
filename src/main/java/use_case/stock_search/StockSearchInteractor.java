package use_case.stock_search;

import entity.StockQuote;

public class StockSearchInteractor implements StockSearchInputBoundary {

    private final StockSearchDataAccessInterface dataAccess;
    private final StockSearchOutputBoundary presenter;

    public StockSearchInteractor(StockSearchDataAccessInterface dataAccess,
                                 StockSearchOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(StockSearchRequestModel requestModel) {
        String symbol = requestModel.getSymbol();

        if (symbol == null || symbol.trim().isEmpty()) {
            presenter.prepareFailView("Please enter a symbol.");
            return;
        }

        symbol = symbol.trim().toUpperCase();

        try {
            StockQuote quote = dataAccess.loadQuote(symbol);

            StockSearchResponseModel responseModel = new StockSearchResponseModel(
                    quote.getSymbol(),
                    quote.getCompanyName(),
                    quote.getExchange(),
                    quote.getIndustry(),
                    quote.getMarketCap(),
                    quote.getCurrentPrice(),
                    quote.getOpen(),
                    quote.getHigh(),
                    quote.getLow(),
                    quote.getPreviousClose()
            );
            presenter.prepareSuccessView(responseModel);
        }
        catch (Exception e) {
            presenter.prepareFailView("Unable to load quote. " + e.getMessage());
        }
    }
}
