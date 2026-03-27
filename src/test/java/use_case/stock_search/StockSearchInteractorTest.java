package use_case.stock_search;

import entity.StockQuote;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockSearchInteractorTest {

    /**
     * Simple in-memory stub for StockSearchDataAccessInterface.
     */
    private static class InMemoryStockSearchDao implements StockSearchDataAccessInterface {

        StockQuote quoteToReturn;
        boolean throwOnLoad = false;
        String lastSymbol;

        @Override
        public StockQuote loadQuote(String symbol) throws Exception {
            lastSymbol = symbol;
            if (throwOnLoad) {
                throw new Exception("Load error");
            }
            return quoteToReturn;
        }
    }

    /**
     * Recording presenter that captures the last success or error.
     */
    private static class RecordingPresenter implements StockSearchOutputBoundary {

        StockSearchResponseModel lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(StockSearchResponseModel responseModel) {
            lastSuccess = responseModel;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            lastError = errorMessage;
        }
    }

    private StockQuote sampleQuote(String symbol) {
        return new StockQuote(
                symbol,
                "Sample Company",
                "NASDAQ",
                "Technology",
                1000.0,
                150.0,
                148.0,
                151.0,
                147.5,
                149.0,
                1_700_000_000L
        );
    }

    @Test
    void execute_success_loadsQuoteAndCallsPresenter() throws Exception {
        // Arrange
        InMemoryStockSearchDao dao = new InMemoryStockSearchDao();
        RecordingPresenter presenter = new RecordingPresenter();
        dao.quoteToReturn = sampleQuote("AAPL");

        StockSearchInteractor interactor = new StockSearchInteractor(dao, presenter);

        StockSearchRequestModel request = new StockSearchRequestModel("aapl");

        // Act
        interactor.execute(request);

        // Assert DAO was called with trimmed/uppercased symbol
        assertEquals("AAPL", dao.lastSymbol);

        // Assert presenter received success
        assertNotNull(presenter.lastSuccess);
        assertNull(presenter.lastError);

        StockSearchResponseModel response = presenter.lastSuccess;
        assertEquals("AAPL", response.getSymbol());
        assertEquals("Sample Company", response.getCompanyName());
        assertEquals("NASDAQ", response.getExchange());
        assertEquals("Technology", response.getIndustry());
        assertEquals(1000.0, response.getMarketCap());
        assertEquals(150.0, response.getCurrentPrice());
    }

    @Test
    void execute_blankSymbol_reportsErrorAndDoesNotCallDao() throws Exception {
        // Arrange
        InMemoryStockSearchDao dao = new InMemoryStockSearchDao();
        RecordingPresenter presenter = new RecordingPresenter();

        StockSearchInteractor interactor = new StockSearchInteractor(dao, presenter);

        StockSearchRequestModel request = new StockSearchRequestModel("   ");

        // Act
        interactor.execute(request);

        // Assert: DAO should not be called
        assertNull(dao.lastSymbol);

        // Assert: error message from presenter
        assertNull(presenter.lastSuccess);
        assertEquals("Please enter a symbol.", presenter.lastError);
    }

    @Test
    void execute_daoThrowsException_reportsError() throws Exception {
        // Arrange
        InMemoryStockSearchDao dao = new InMemoryStockSearchDao();
        RecordingPresenter presenter = new RecordingPresenter();
        dao.throwOnLoad = true;

        StockSearchInteractor interactor = new StockSearchInteractor(dao, presenter);

        StockSearchRequestModel request = new StockSearchRequestModel("AAPL");

        // Act
        interactor.execute(request);

        // Assert: presenter should receive an error message
        assertNull(presenter.lastSuccess);
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.startsWith("Unable to load quote."));
    }
}
