package use_case.news;

import entity.NewsArticle;
import java.util.List;

/**
 * Implement "what happens when user asks for news"
 * <p>
 * 1. Validate the NewsRequestModel
 * 2. Call NewsDataAccessInterface
 * 3. Build a NewsResponse Model
 * 4. Call the presenter -- NewsOutputBoundary
 */
public class NewsInteractor implements NewsInputBoundary{
    private final NewsDataAccessInterface newsDataAccess;
    private final NewsOutputBoundary newsPresenter;

    public NewsInteractor(NewsDataAccessInterface newsDataAccess,
                          NewsOutputBoundary newsPresenter) {
        this.newsDataAccess = newsDataAccess;
        this.newsPresenter = newsPresenter;
    }

    @Override
    public void executeMarketNews(NewsRequestModel requestModel) {
        try {
            List<NewsArticle> articles = newsDataAccess.loadMarketNews();

            if (articles == null || articles.isEmpty()) {
                newsPresenter.prepareFailView("No market news available.");
                return;
            }

            NewsResponseModel response = new NewsResponseModel(articles);
            newsPresenter.prepareSuccessView(response);

        } catch (Exception e) {
            newsPresenter.prepareFailView("Unable to load market news: " + e.getMessage());
        }
    }

    @Override
    public void executeCompanyNews(NewsRequestModel requestModel) {
        String symbol = requestModel.getSymbol();

        if (symbol == null || symbol.isBlank()) {
            newsPresenter.prepareFailView("Symbol is required for company news.");
            return;
        }

        try {
            List<NewsArticle> articles = newsDataAccess.loadCompanyNews(
                    symbol,
                    requestModel.getFromDate(),
                    requestModel.getToDate()
            );

            if (articles == null || articles.isEmpty()) {
                newsPresenter.prepareFailView("No news found for symbol: " + symbol);
                return;
            }

            NewsResponseModel response = new NewsResponseModel(articles);
            newsPresenter.prepareSuccessView(response);

        } catch (Exception e) {
            newsPresenter.prepareFailView("Unable to load company news: " + e.getMessage());
        }
    }
}
