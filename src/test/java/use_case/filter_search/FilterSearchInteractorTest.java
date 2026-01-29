package use_case.filter_search;

import entity.Stock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FilterSearchInteractorTest {

    private static class InMemoryFSDAO implements FilterSearchDataAccessInterface  {
        List<Stock> result = new ArrayList<>();
        Boolean throwException = false;

        String currency;
        String mic;
        String type;
        String exchange;

        @Override
        public List<Stock> loadStocks(String exchange, String mic, String securityType, String currency) throws Exception {
            this.currency = currency;
            this.mic = mic;
            this.type = securityType;
            this.exchange = exchange;

            if (throwException) {
                throw new Exception("Unable to load filtered stocks.");
            }
            return result;
        }
    }

    public static class RecordingPresenter implements FilterSearchOutputBoundary {

        FilterSearchResponse response;
        String error;

        @Override
        public void prepareSuccessView(FilterSearchResponse filterSearchResponse) {
            response = filterSearchResponse;
        }

        @Override
        public void prepareFailView(String s) {
            error = s;
        }
    }

    private Stock exampleStock() {
        return new Stock(
                "USD",
                "TACTICAL RESOURCE",
                "CIEN",
                "BBG000BP1152",
                "OOTC",
                "CIEN",
                "Common Stock");
    }


    @Test
    void successTest() {
        InMemoryFSDAO inMemoryFSDAO = new InMemoryFSDAO();
        RecordingPresenter recordingPresenter = new  RecordingPresenter();

        inMemoryFSDAO.result = List.of(exampleStock());

        FilterSearchInteractor interactor = new FilterSearchInteractor(inMemoryFSDAO, recordingPresenter);
        FilterSearchRequest request = new FilterSearchRequest("US", "OOTC",
                "Common Stock", "USD");

        interactor.execute(request);

        assertEquals("USD", inMemoryFSDAO.currency);
        assertEquals("OOTC", inMemoryFSDAO.mic);
        assertEquals("Common Stock", inMemoryFSDAO.type);
        assertEquals("US", inMemoryFSDAO.exchange);

        assertEquals(null, recordingPresenter.error);
        assertNotNull(recordingPresenter.response);
        assertEquals(inMemoryFSDAO.result.size(), 1);

        Stock res = inMemoryFSDAO.result.get(0);

        assertEquals(res.getCurrency(), exampleStock().getCurrency());
        assertEquals(res.getMic(), exampleStock().getMic());
        assertEquals(res.getType(), exampleStock().getType());
        assertEquals(res.getDescription(), exampleStock().getDescription());
        assertEquals(res.getSymbol(), exampleStock().getSymbol());
        assertEquals(res.getDisplaySymbol(), exampleStock().getDisplaySymbol());
        assertEquals(res.getFigi(), exampleStock().getFigi());

    }

    @Test
    void failTest() {
        InMemoryFSDAO inMemoryFSDAO = new InMemoryFSDAO();
        RecordingPresenter recordingPresenter = new  RecordingPresenter();
        inMemoryFSDAO.throwException = true;

        inMemoryFSDAO.result = List.of();

        FilterSearchInteractor interactor = new FilterSearchInteractor(inMemoryFSDAO, recordingPresenter);
        FilterSearchRequest request = new FilterSearchRequest("US", "OOTC",
                "ERROR", "USD");

        interactor.execute(request);

        assertEquals("Unable to load filtered stocks.", recordingPresenter.error);

    }
}

