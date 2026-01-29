package use_case.market_status;

import entity.MarketStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MarketStatusInteractor.
 */
public class MarketStatusInteractorTest {

    /**
     * Simple in-memory stub for MarketStatusDataAccessInterface.
     */
    private static class InMemoryMarketStatusDao implements MarketStatusDataAccessInterface {

        MarketStatus statusToReturn;
        boolean throwOnLoad = false;
        int loadCalls = 0;

        @Override
        public MarketStatus loadStatus() throws Exception {
            loadCalls++;
            if (throwOnLoad) {
                throw new Exception("Data access error");
            }
            return statusToReturn;
        }
    }

    /**
     * Recording presenter that just stores what the interactor sends.
     */
    private static class RecordingPresenter implements MarketStatusOutputBoundary {

        MarketStatusResponseModel lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(MarketStatusResponseModel responseModel) {
            lastSuccess = responseModel;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            lastError = errorMessage;
        }
    }

    @Test
    void execute_success_openMarket() {
        // Arrange
        InMemoryMarketStatusDao dao = new InMemoryMarketStatusDao();
        RecordingPresenter presenter = new RecordingPresenter();

        dao.statusToReturn = new MarketStatus(
                "US",
                true,                 // open
                "regular",            // session
                "",                   // holiday
                1_700_000_000L,       // timestamp
                "America/New_York"    // timezone
        );

        MarketStatusInteractor interactor =
                new MarketStatusInteractor(dao, presenter);

        // Act
        interactor.execute();

        // Assert DAO called
        assertEquals(1, dao.loadCalls, "DAO should be called exactly once");

        // Assert presenter got success, not error
        assertNull(presenter.lastError, "No error should be reported");
        assertNotNull(presenter.lastSuccess, "Success response should be sent");

        MarketStatus status = presenter.lastSuccess.getMarketStatus();
        assertNotNull(status);
        assertEquals("US", status.getExchange());
        assertTrue(status.isOpen());
        assertEquals("regular", status.getSession());
    }

    @Test
    void execute_success_closedHoliday() {
        // Arrange
        InMemoryMarketStatusDao dao = new InMemoryMarketStatusDao();
        RecordingPresenter presenter = new RecordingPresenter();

        dao.statusToReturn = new MarketStatus(
                "US",
                false,                // closed
                "",                   // session
                "Memorial Day",       // holiday
                1_700_000_000L,
                "America/New_York"
        );

        MarketStatusInteractor interactor =
                new MarketStatusInteractor(dao, presenter);

        // Act
        interactor.execute();

        // Assert
        assertEquals(1, dao.loadCalls);
        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        MarketStatus status = presenter.lastSuccess.getMarketStatus();
        assertFalse(status.isOpen());
        assertEquals("Memorial Day", status.getHoliday());
    }

    @Test
    void execute_nullStatus_triggersFailView() {
        // Arrange
        InMemoryMarketStatusDao dao = new InMemoryMarketStatusDao();
        RecordingPresenter presenter = new RecordingPresenter();

        dao.statusToReturn = null; // simulate no data

        MarketStatusInteractor interactor =
                new MarketStatusInteractor(dao, presenter);

        // Act
        interactor.execute();

        // Assert
        assertEquals(1, dao.loadCalls);
        assertNull(presenter.lastSuccess, "No success should be sent");
        assertNotNull(presenter.lastError, "Error should be reported");
        assertTrue(presenter.lastError.contains("No market status available"),
                "Error message should mention missing status");
    }

    @Test
    void execute_daoThrows_triggersFailView() {
        // Arrange
        InMemoryMarketStatusDao dao = new InMemoryMarketStatusDao();
        RecordingPresenter presenter = new RecordingPresenter();

        dao.throwOnLoad = true;

        MarketStatusInteractor interactor =
                new MarketStatusInteractor(dao, presenter);

        // Act
        interactor.execute();

        // Assert
        assertEquals(1, dao.loadCalls);
        assertNull(presenter.lastSuccess);
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.contains("Unable to load market status"),
                "Error message should mention inability to load status");
    }
}