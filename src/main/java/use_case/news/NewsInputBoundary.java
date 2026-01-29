package use_case.news;

/**
 * Define what actions this user case supports
 */
public interface NewsInputBoundary {
    // Load general market news (category=general).
    void executeMarketNews(NewsRequestModel requestModel);

    // Load company-specific news for the given symbol.
    void executeCompanyNews(NewsRequestModel requestModel);
}
