package use_case.news;

/**
 * Request model for news.
 * For market news, symbol/fromDate/toDate can all be null.
 * For company news, symbol must be non-null; dates may be null (use default range).
 * Input Data holder, the data comes from the UI
 */
public class NewsRequestModel {

    private final String symbol;    // e.g. "AAPL" or null for market news
    private final String fromDate;  // "YYYY-MM-DD" or null
    private final String toDate;    // "YYYY-MM-DD" or null

    // Constructor for general market news
    public NewsRequestModel() {
        this.symbol = null;
        this.fromDate = null;
        this.toDate = null;
    }

    // Constructor for company news
    public NewsRequestModel(String symbol, String fromDate, String toDate) {
        this.symbol = symbol;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    // Market news don't have a symbol
    public boolean isMarketNewsRequest() {
        return symbol == null || symbol.isBlank();
    }
}

