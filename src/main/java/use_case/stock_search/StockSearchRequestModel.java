package use_case.stock_search;

public class StockSearchRequestModel {
    private final String symbol;

    public StockSearchRequestModel(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
