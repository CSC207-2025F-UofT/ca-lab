package interface_adapter.news;

import use_case.news.NewsInputBoundary;
import use_case.news.NewsRequestModel;

/**
 * read from UI
 * build a NewsRequestModel
 * "send" the request through input boundary
 */
public class NewsController {

    private final NewsInputBoundary interactor;

    public NewsController(NewsInputBoundary interactor) {
        this.interactor = interactor;
    }

    // Load general market news (category=general)
    public void loadMarketNews() {
        NewsRequestModel request = new NewsRequestModel(); // market news ctor
        interactor.executeMarketNews(request);
    }

    // Load company-specific news (for a symbol like "AAPL")
    public void loadCompanyNews(String symbol, String fromDate, String toDate) {
        NewsRequestModel request = new NewsRequestModel(symbol, fromDate, toDate);
        interactor.executeCompanyNews(request);
    }
}

