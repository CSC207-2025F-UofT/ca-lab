package use_case.trade;

/**
 * Request model for trade feed connection.
 */
public class TradeRequestModel {
    private final String symbol;
    
    public TradeRequestModel(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
}

