package use_case.earnings_history;

public interface GetEarningsHistoryOutputBoundary {

    void prepareSuccessView(GetEarningsHistoryOutputData outputData);

    // These three correspond to the three different JOptionPane cases
    void prepareSymbolErrorView(String errorMessage);

    void prepareNoDataView(String message);

    void prepareConnectionErrorView(String message);
}
