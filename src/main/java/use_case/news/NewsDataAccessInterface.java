package use_case.news;

import java.util.List;
import entity.NewsArticle;

public interface NewsDataAccessInterface {

    // Maps to Finnhub endpoint: /news?category=general
    List<NewsArticle> loadMarketNews() throws Exception;

    //Maps to Finnhub endpoint:
    //     /company-news?symbol={symbol}&from={fromDate}&to={toDate}
    //     Dates are "YYYY-MM-DD".
    List<NewsArticle> loadCompanyNews(String symbol,
                                      String fromDate,
                                      String toDate) throws Exception;
}