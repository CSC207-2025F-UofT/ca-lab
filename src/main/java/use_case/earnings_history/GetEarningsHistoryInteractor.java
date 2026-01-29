package use_case.earnings_history;

import entity.EarningsRecord;

import java.io.IOException;
import java.util.List;

public class GetEarningsHistoryInteractor implements GetEarningsHistoryInputBoundary {

    private final EarningsDataAccessInterface dataAccess;
    private final GetEarningsHistoryOutputBoundary presenter;

    public GetEarningsHistoryInteractor(EarningsDataAccessInterface dataAccess,
                                        GetEarningsHistoryOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(GetEarningsHistoryInputData inputData) {
        String symbol = inputData.getSymbol();

        if (symbol == null || symbol.trim().isEmpty()) {
            presenter.prepareSymbolErrorView("Please enter a symbol.");
            return;
        }

        symbol = symbol.trim().toUpperCase();

        try {
            // method name matches the interface
            List<EarningsRecord> records = dataAccess.getEarningsFor(symbol);

            if (records == null) {
                presenter.prepareSymbolErrorView(
                        "Company not found or symbol not supported.\nPlease check the symbol.");
            } else if (records.isEmpty()) {
                presenter.prepareNoDataView(
                        "No earnings data found for this company.");
            } else {
                GetEarningsHistoryOutputData outputData =
                        new GetEarningsHistoryOutputData(symbol, records);
                presenter.prepareSuccessView(outputData);
            }
        } catch (IOException e) {
            presenter.prepareConnectionErrorView(
                    "Network error or API issue.\nCheck your internet or API key.");
        }
    }
}
