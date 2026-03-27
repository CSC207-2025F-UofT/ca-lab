package interface_adapter.filter_search;

import use_case.filter_search.FilterSearchInputBoundary;
import use_case.filter_search.FilterSearchRequest;

public class FilterSearchController {

    private final FilterSearchInputBoundary interactor;

    public FilterSearchController(FilterSearchInputBoundary filterSearchInputBoundary) {
        this.interactor = filterSearchInputBoundary;
    }

    /**
     * Executes the Change Password Use Case.
     * @param exchange the exchange
     * @param mic the mic
     * @param securityType the security type
     * @param currency the currency
     */
    public void loadStocks(String exchange, String mic, String securityType, String currency) {
        final FilterSearchRequest request = new FilterSearchRequest(exchange, mic, securityType, currency);
        interactor.execute(request);
    }
}
