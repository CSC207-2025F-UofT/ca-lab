package use_case.filter_search;

import entity.Stock;

import java.util.List;

public class FilterSearchResponse {
    private final List<Stock> stocks;

    public FilterSearchResponse(List<Stock> stocks) {
        System.out.println("RESPONSE ctor: incoming stocks = " +
                (stocks == null ? "null" : stocks.size()));
        this.stocks = stocks;
    }

    public List<Stock> getStocks() {
        return stocks;
    }
}
