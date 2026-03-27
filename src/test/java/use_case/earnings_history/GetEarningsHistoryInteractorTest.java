package use_case.earnings_history;

import entity.EarningsRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GetEarningsHistoryInteractor.
 */
public class GetEarningsHistoryInteractorTest {

    /**
     * Simple in-memory stub for EarningsDataAccessInterface.
     * Configure its fields before each test.
     */
    private static class InMemoryEarningsDao implements EarningsDataAccessInterface {

        List<EarningsRecord> recordsToReturn = new ArrayList<>();
        boolean throwException = false;

        String lastSymbol;

        @Override
        public List<EarningsRecord> getEarningsFor(String symbol) throws IOException {
            lastSymbol = symbol;
            if (throwException) {
                throw new IOException("boom");
            }
            return recordsToReturn;
        }
    }

    /**
     * Recording presenter that stores everything the interactor sends.
     * It implements **all** methods in GetEarningsHistoryOutputBoundary.
     */
    private static class RecordingPresenter implements GetEarningsHistoryOutputBoundary {

        GetEarningsHistoryOutputData lastSuccess;
        String lastSymbolErrorMessage;
        String lastConnectionErrorMessage;
        String lastNoDataMessage;

        @Override
        public void prepareSuccessView(GetEarningsHistoryOutputData outputData) {
            lastSuccess = outputData;
        }

        @Override
        public void prepareSymbolErrorView(String message) {
            lastSymbolErrorMessage = message;
        }

        @Override
        public void prepareConnectionErrorView(String message) {
            lastConnectionErrorMessage = message;
        }

        @Override
        public void prepareNoDataView(String message) {
            lastNoDataMessage = message;
        }
    }

    private EarningsRecord sampleRecord(String period) {
        return new EarningsRecord(period, 1.23, 1.00, 0.23);
    }

    @Test
    void blankSymbol_reportsSymbolError_andDoesNotCallDao() {
        InMemoryEarningsDao dao = new InMemoryEarningsDao();
        RecordingPresenter presenter = new RecordingPresenter();
        GetEarningsHistoryInteractor interactor =
                new GetEarningsHistoryInteractor(dao, presenter);

        // symbol is blank → invalid
        GetEarningsHistoryInputData input =
                new GetEarningsHistoryInputData("   ");

        interactor.execute(input);

        assertNull(dao.lastSymbol, "DAO must not be called for blank symbol");
        assertNull(presenter.lastSuccess, "No success result expected");
        assertNull(presenter.lastConnectionErrorMessage);
        assertNull(presenter.lastNoDataMessage);
        assertNotNull(presenter.lastSymbolErrorMessage, "Symbol error should be reported");
        assertTrue(presenter.lastSymbolErrorMessage.toLowerCase().contains("symbol"),
                "Error message should mention symbol");
    }

    @Test
    void daoThrowsIOException_reportsConnectionError() {
        InMemoryEarningsDao dao = new InMemoryEarningsDao();
        dao.throwException = true;

        RecordingPresenter presenter = new RecordingPresenter();
        GetEarningsHistoryInteractor interactor =
                new GetEarningsHistoryInteractor(dao, presenter);

        GetEarningsHistoryInputData input =
                new GetEarningsHistoryInputData("AAPL");

        interactor.execute(input);

        assertEquals("AAPL", dao.lastSymbol);
        assertNull(presenter.lastSuccess);
        assertNull(presenter.lastSymbolErrorMessage);
        assertNull(presenter.lastNoDataMessage);
        assertNotNull(presenter.lastConnectionErrorMessage,
                "Connection/API error should be reported");
    }

    @Test
    void daoReturnsNull_SymbolNotSupported() {
        InMemoryEarningsDao dao = new InMemoryEarningsDao();
        dao.recordsToReturn = null;

        RecordingPresenter presenter = new RecordingPresenter();
        GetEarningsHistoryInteractor interactor =
                new GetEarningsHistoryInteractor(dao, presenter);

        GetEarningsHistoryInputData input =
                new GetEarningsHistoryInputData("AAPL");

        interactor.execute(input);

        assertEquals("AAPL", dao.lastSymbol);
        assertNull(presenter.lastSuccess);
        assertNull(presenter.lastConnectionErrorMessage);
        assertNull(presenter.lastNoDataMessage);
        assertNotNull(presenter.lastSymbolErrorMessage);
        String msg = presenter.lastSymbolErrorMessage.toLowerCase();
        assertTrue(msg.contains("not found") || msg.contains("not supported"),
                "Error should mention company not found/unsupported");
    }

    @Test
    void daoReturnsEmptyList_reportsNoData() {
        InMemoryEarningsDao dao = new InMemoryEarningsDao();
        dao.recordsToReturn = List.of();  // valid symbol, but no earnings

        RecordingPresenter presenter = new RecordingPresenter();
        GetEarningsHistoryInteractor interactor =
                new GetEarningsHistoryInteractor(dao, presenter);

        GetEarningsHistoryInputData input =
                new GetEarningsHistoryInputData("AAPL");

        interactor.execute(input);

        assertEquals("AAPL", dao.lastSymbol);
        assertNull(presenter.lastSuccess);
        assertNull(presenter.lastSymbolErrorMessage);
        assertNull(presenter.lastConnectionErrorMessage);
        assertNotNull(presenter.lastNoDataMessage);
        String msg = presenter.lastNoDataMessage.toLowerCase();
        assertTrue(msg.contains("no earnings") || msg.contains("no data"),
                "No-data message should mention missing earnings");
    }

    @Test
    void daoReturnsRecords_callsSuccessWithRecords() {
        InMemoryEarningsDao dao = new InMemoryEarningsDao();
        dao.recordsToReturn = List.of(
                sampleRecord("2024-Q1"),
                sampleRecord("2023-Q4")
        );

        RecordingPresenter presenter = new RecordingPresenter();
        GetEarningsHistoryInteractor interactor =
                new GetEarningsHistoryInteractor(dao, presenter);

        GetEarningsHistoryInputData input =
                new GetEarningsHistoryInputData("AAPL");

        interactor.execute(input);

        assertEquals("AAPL", dao.lastSymbol);
        assertNull(presenter.lastSymbolErrorMessage);
        assertNull(presenter.lastConnectionErrorMessage);
        assertNull(presenter.lastNoDataMessage);
        assertNotNull(presenter.lastSuccess, "Success output expected");

        // GetEarningsHistoryOutputData.getSymbol()
        assertEquals("AAPL", presenter.lastSuccess.getSymbol());

        List<EarningsRecord> records = presenter.lastSuccess.getRecords();
        assertEquals(2, records.size());
        assertEquals("2024-Q1", records.get(0).getPeriod());
        assertEquals("2023-Q4", records.get(1).getPeriod());
    }
}