package use_case.earnings_history;

public class GetEarningsHistoryInputData {

    private final String symbol;

    public GetEarningsHistoryInputData(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
