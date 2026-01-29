package use_case.filter_search;

import entity.Stock;

import java.util.List;

public interface FilterSearchDataAccessInterface {
    List<Stock> loadStocks(String exchange, String mic, String securityType, String currency) throws Exception;
}

