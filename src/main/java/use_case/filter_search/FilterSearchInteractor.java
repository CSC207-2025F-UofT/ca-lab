package use_case.filter_search;

import entity.Stock;

import java.util.List;

/**
 * The Filter Search Interactor.
 */

public class FilterSearchInteractor implements FilterSearchInputBoundary {
    private final FilterSearchDataAccessInterface filterSearchDataAccess;
    private final FilterSearchOutputBoundary filterSearchPresenter;

    public FilterSearchInteractor(FilterSearchDataAccessInterface data,
                                  FilterSearchOutputBoundary filterSearchOutputBoundary) {
        this.filterSearchDataAccess = data;
        this.filterSearchPresenter = filterSearchOutputBoundary;
    }

    @Override
    public void execute(FilterSearchRequest filterSearchRequest) {
        final String exchange =  filterSearchRequest.getExchange();
        final String mic =  filterSearchRequest.getMic();
        final String securityType = filterSearchRequest.getSecurityType();
        final String currency =  filterSearchRequest.getCurrency();

        try {
            List<Stock> stocks =
                    filterSearchDataAccess.loadStocks(exchange, mic, securityType, currency);

            System.out.println("INTERACTOR: stocks from DAO = " +
                    (stocks == null ? "null" : stocks.size()));

            FilterSearchResponse response =  new FilterSearchResponse(stocks);
            filterSearchPresenter.prepareSuccessView(response);
        }

        catch (Exception e) {
            // Debug output in console:
            e.printStackTrace();

            filterSearchPresenter.prepareFailView("Unable to load filtered stocks."
                    +  e.getMessage());
        }
    }
}
