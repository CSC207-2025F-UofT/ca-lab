package use_case.news;

import entity.NewsArticle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NewsInteractor.
 */
public class NewsInteractorTest {

    /**
     * Simple in-memory stub for NewsDataAccessInterface.
     * You configure its fields before running each test.
     */
    private static class InMemoryNewsDao implements NewsDataAccessInterface {

        List<NewsArticle> marketNewsToReturn = new ArrayList<>();
        List<NewsArticle> companyNewsToReturn = new ArrayList<>();

        boolean throwOnMarket = false;
        boolean throwOnCompany = false;

        // capture last call arguments
        String lastCompanySymbol;
        String lastCompanyFrom;
        String lastCompanyTo;

        @Override
        public List<NewsArticle> loadMarketNews() throws Exception {
            if (throwOnMarket) {
                throw new Exception("Market error");
            }
            return marketNewsToReturn;
        }

        @Override
        public List<NewsArticle> loadCompanyNews(String symbol, String fromDate, String toDate) throws Exception {
            lastCompanySymbol = symbol;
            lastCompanyFrom = fromDate;
            lastCompanyTo = toDate;

            if (throwOnCompany) {
                throw new Exception("Company error");
            }
            return companyNewsToReturn;
        }
    }

    /**
     * Recording presenter that just stores what the interactor sends.
     */
    private static class RecordingPresenter implements NewsOutputBoundary {

        NewsResponseModel lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(NewsResponseModel responseModel) {
            lastSuccess = responseModel;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            lastError = errorMessage;
        }
    }

    private NewsArticle sampleArticle(String symbol) {
        return new NewsArticle(
                1L,
                "general",
                1_700_000_000L,
                "Test headline for " + symbol,
                "",
                symbol,
                "TestSource",
                "Test summary",
                "https://example.com/article"
        );
    }

    @Test
    void executeMarketNews_success_callsPresenterWithArticles() {
        // Arrange
        InMemoryNewsDao dao = new InMemoryNewsDao();
        RecordingPresenter presenter = new RecordingPresenter();
        dao.marketNewsToReturn = List.of(sampleArticle("AAPL"), sampleArticle("MSFT"));

        NewsInteractor interactor = new NewsInteractor(dao, presenter);

        NewsRequestModel request = new NewsRequestModel(); // market news

        // Act
        interactor.executeMarketNews(request);

        // Assert
        assertNull(presenter.lastError, "No error should be reported");
        assertNotNull(presenter.lastSuccess, "Success response should be sent");
        List<NewsArticle> articles = presenter.lastSuccess.getArticles();
        assertEquals(2, articles.size());
        assertEquals("AAPL", articles.get(0).getRelated());
        assertEquals("MSFT", articles.get(1).getRelated());
    }

    @Test
    void executeMarketNews_error_callsFailView() {
        // Arrange
        InMemoryNewsDao dao = new InMemoryNewsDao();
        RecordingPresenter presenter = new RecordingPresenter();
        dao.throwOnMarket = true;

        NewsInteractor interactor = new NewsInteractor(dao, presenter);
        NewsRequestModel request = new NewsRequestModel();

        // Act
        interactor.executeMarketNews(request);

        // Assert
        assertNull(presenter.lastSuccess, "No success response expected");
        assertNotNull(presenter.lastError, "Error should be reported");
        assertTrue(presenter.lastError.contains("Unable to load market news"),
                "Error message should mention market news");
    }

    @Test
    void executeCompanyNews_emptySymbol_reportsError() {
        // Arrange
        InMemoryNewsDao dao = new InMemoryNewsDao();
        RecordingPresenter presenter = new RecordingPresenter();
        NewsInteractor interactor = new NewsInteractor(dao, presenter);

        // symbol is blank → invalid
        NewsRequestModel request = new NewsRequestModel("  ", "2024-01-01", "2024-01-31");

        // Act
        interactor.executeCompanyNews(request);

        // Assert
        assertNull(presenter.lastSuccess, "No success response expected");
        assertEquals("Symbol is required for company news.", presenter.lastError);
        // Also check DAO was never called
        assertNull(dao.lastCompanySymbol, "DAO should not be called on invalid symbol");
    }

    @Test
    void executeCompanyNews_success_passesArticlesAndArgs() {
        // Arrange
        InMemoryNewsDao dao = new InMemoryNewsDao();
        RecordingPresenter presenter = new RecordingPresenter();

        dao.companyNewsToReturn = List.of(sampleArticle("AAPL"));

        NewsInteractor interactor = new NewsInteractor(dao, presenter);

        NewsRequestModel request =
                new NewsRequestModel("AAPL", "2024-01-01", "2024-01-31");

        // Act
        interactor.executeCompanyNews(request);

        // Assert DAO was called with correct arguments
        assertEquals("AAPL", dao.lastCompanySymbol);
        assertEquals("2024-01-01", dao.lastCompanyFrom);
        assertEquals("2024-01-31", dao.lastCompanyTo);

        // Assert presenter got success
        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);
        List<NewsArticle> articles = presenter.lastSuccess.getArticles();
        assertEquals(1, articles.size());
        assertEquals("AAPL", articles.get(0).getRelated());
    }

    @Test
    void executeCompanyNews_noArticles_triggersFailView() {
        // Arrange
        InMemoryNewsDao dao = new InMemoryNewsDao();
        RecordingPresenter presenter = new RecordingPresenter();

        dao.companyNewsToReturn = List.of(); // empty list

        NewsInteractor interactor = new NewsInteractor(dao, presenter);

        NewsRequestModel request =
                new NewsRequestModel("AAPL", "2024-01-01", "2024-01-31");

        // Act
        interactor.executeCompanyNews(request);

        // Assert
        assertNull(presenter.lastSuccess);
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.contains("No news found for symbol"),
                "Should report no news found");
    }

    @Test
    void executeMarketNews_noArticles_triggersFailView() {
        InMemoryNewsDao dao = new InMemoryNewsDao();
        RecordingPresenter presenter = new RecordingPresenter();

        // simulate "no market news"
        dao.marketNewsToReturn = List.of();   // or null if you want to cover that

        NewsInteractor interactor = new NewsInteractor(dao, presenter);
        NewsRequestModel request = new NewsRequestModel();

        interactor.executeMarketNews(request);

        assertNull(presenter.lastSuccess, "No success response expected");
        assertNotNull(presenter.lastError, "Error should be reported");
        assertTrue(presenter.lastError.contains("No market news available"),
                "Error message should mention no market news");
    }

    @Test
    void executeCompanyNews_daoThrows_triggersFailView() {
        InMemoryNewsDao dao = new InMemoryNewsDao();
        RecordingPresenter presenter = new RecordingPresenter();

        dao.throwOnCompany = true;

        NewsInteractor interactor = new NewsInteractor(dao, presenter);

        NewsRequestModel request =
                new NewsRequestModel("AAPL", "2024-01-01", "2024-01-31");

        interactor.executeCompanyNews(request);

        assertNull(presenter.lastSuccess, "No success response expected");
        assertNotNull(presenter.lastError, "Error should be reported");
        assertTrue(presenter.lastError.contains("Unable to load company news"),
                "Error message should mention inability to load company news");
    }
}
