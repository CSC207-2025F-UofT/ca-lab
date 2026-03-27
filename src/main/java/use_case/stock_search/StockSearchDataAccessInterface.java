package use_case.stock_search;

import entity.StockQuote;

public interface StockSearchDataAccessInterface {
    StockQuote loadQuote(String symbol) throws Exception;
}