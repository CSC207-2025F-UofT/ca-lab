package use_case.trade;

import entity.Trade;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TradeInteractor.
 */
class TradeInteractorTest {

    /**
     * Simple in-memory stub for TradeDataAccessInterface.
     */
    private static class InMemoryTradeDataAccess implements TradeDataAccessInterface {

        TradeListener storedListener;
        String lastSymbol;
        boolean throwOnConnect = false;
        boolean throwOnDisconnect = false;
        int connectCalls = 0;
        int disconnectCalls = 0;

        @Override
        public void connect(String symbol, TradeListener listener) {
            connectCalls++;
            lastSymbol = symbol;
            storedListener = listener;
            if (throwOnConnect) {
                throw new RuntimeException("Connection error");
            }
        }

        @Override
        public void disconnect() {
            disconnectCalls++;
            if (throwOnDisconnect) {
                throw new RuntimeException("Disconnection error");
            }
        }
    }

    /**
     * Recording presenter that captures the last success, error, or status update.
     */
    private static class RecordingPresenter implements TradeOutputBoundary {

        TradeResponseModel lastSuccess;
        String lastError;
        String lastStatusText;
        Boolean lastStatusIsError;

        @Override
        public void prepareSuccessView(TradeResponseModel responseModel) {
            lastSuccess = responseModel;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            lastError = errorMessage;
        }

        @Override
        public void prepareStatusView(String statusText, boolean isError) {
            lastStatusText = statusText;
            lastStatusIsError = isError;
        }
    }

    @Test
    void execute_success_validSymbol() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);
        TradeRequestModel requestModel = new TradeRequestModel("AAPL");

        // Act
        interactor.execute(requestModel);

        // Assert
        assertEquals(1, dataAccess.connectCalls, "DAO should be called exactly once");
        assertEquals("AAPL", dataAccess.lastSymbol, "DAO should receive the correct symbol");
        assertNotNull(dataAccess.storedListener, "Listener should be stored");
        assertNull(presenter.lastError, "No error should be reported");
    }

    @Test
    void execute_failure_emptySymbol() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);
        TradeRequestModel requestModel = new TradeRequestModel("");

        // Act
        interactor.execute(requestModel);

        // Assert
        assertEquals(0, dataAccess.connectCalls, "DAO should not be called");
        assertNotNull(presenter.lastError, "Error should be reported");
        assertEquals("Please enter a symbol.", presenter.lastError);
        assertNull(presenter.lastSuccess, "No success should be sent");
    }

    @Test
    void execute_failure_nullSymbol() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);
        TradeRequestModel requestModel = new TradeRequestModel(null);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertEquals(0, dataAccess.connectCalls, "DAO should not be called");
        assertNotNull(presenter.lastError, "Error should be reported");
        assertEquals("Please enter a symbol.", presenter.lastError);
    }

    @Test
    void execute_failure_whitespaceOnlySymbol() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);
        TradeRequestModel requestModel = new TradeRequestModel("   ");

        // Act
        interactor.execute(requestModel);

        // Assert
        assertEquals(0, dataAccess.connectCalls, "DAO should not be called");
        assertNotNull(presenter.lastError, "Error should be reported");
        assertEquals("Please enter a symbol.", presenter.lastError);
    }

    @Test
    void execute_success_trimsWhitespace() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);
        TradeRequestModel requestModel = new TradeRequestModel("  AAPL  ");

        // Act
        interactor.execute(requestModel);

        // Assert
        assertEquals(1, dataAccess.connectCalls);
        assertEquals("AAPL", dataAccess.lastSymbol, "Symbol should be trimmed");
    }

    @Test
    void execute_failure_connectionException() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();
        dataAccess.throwOnConnect = true;

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);
        TradeRequestModel requestModel = new TradeRequestModel("AAPL");

        // Act
        interactor.execute(requestModel);

        // Assert
        assertEquals(1, dataAccess.connectCalls);
        assertNotNull(presenter.lastError, "Error should be reported");
        assertTrue(presenter.lastError.contains("Unable to connect to trade feed"),
                "Error message should mention connection failure");
        assertTrue(presenter.lastError.contains("Connection error"),
                "Error message should include exception message");
    }

    @Test
    void execute_success_listenerOnTradeCalled() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);
        TradeRequestModel requestModel = new TradeRequestModel("BINANCE:BTCUSDT");

        // Act
        interactor.execute(requestModel);

        // Simulate trade data arriving via listener callback
        Trade trade = new Trade("BINANCE:BTCUSDT", 50000.0, 1.5, Instant.now());
        dataAccess.storedListener.onTrade(trade);

        // Assert
        assertNotNull(presenter.lastSuccess, "Success view should be prepared");
        assertNotNull(presenter.lastSuccess.getTrade(), "Trade should be in response model");
        assertEquals("BINANCE:BTCUSDT", presenter.lastSuccess.getTrade().getSymbol());
        assertEquals(50000.0, presenter.lastSuccess.getTrade().getPrice());
        assertEquals(1.5, presenter.lastSuccess.getTrade().getVolume());
    }

    @Test
    void execute_success_listenerOnStatusChangedCalled() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);
        TradeRequestModel requestModel = new TradeRequestModel("AAPL");

        // Act
        interactor.execute(requestModel);

        // Simulate status update via listener callback
        dataAccess.storedListener.onStatusChanged("Status: Connected", false);

        // Assert
        assertNotNull(presenter.lastStatusText, "Status should be recorded");
        assertEquals("Status: Connected", presenter.lastStatusText);
        assertFalse(presenter.lastStatusIsError, "Should not be an error status");
    }

    @Test
    void execute_success_listenerOnStatusChangedError() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);
        TradeRequestModel requestModel = new TradeRequestModel("AAPL");

        // Act
        interactor.execute(requestModel);

        // Simulate error status via listener callback
        dataAccess.storedListener.onStatusChanged("Status: Error - Symbol not found", true);

        // Assert
        assertEquals("Status: Error - Symbol not found", presenter.lastStatusText);
        assertTrue(presenter.lastStatusIsError, "Should be an error status");
    }

    @Test
    void disconnect_success() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);

        // Act
        interactor.disconnect();

        // Assert
        assertEquals(1, dataAccess.disconnectCalls, "DAO disconnect should be called");
        assertNull(presenter.lastError, "No error should be reported");
    }

    @Test
    void disconnect_failure_exception() {
        // Arrange
        InMemoryTradeDataAccess dataAccess = new InMemoryTradeDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();
        dataAccess.throwOnDisconnect = true;

        TradeInteractor interactor = new TradeInteractor(dataAccess, presenter);

        // Act
        interactor.disconnect();

        // Assert
        assertEquals(1, dataAccess.disconnectCalls);
        assertNotNull(presenter.lastError, "Error should be reported");
        assertTrue(presenter.lastError.contains("Unable to disconnect"),
                "Error message should mention disconnection failure");
        assertTrue(presenter.lastError.contains("Disconnection error"),
                "Error message should include exception message");
    }
}

