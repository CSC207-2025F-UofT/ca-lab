package use_case.stock_search;

public class StockSearchResponseModel {
    private final String symbol;
    private final String companyName;
    private final String exchange;
    private final String industry;
    private final double marketCap;
    private final double currentPrice;
    private final double open;
    private final double high;
    private final double low;
    private final double previousClose;

    public StockSearchResponseModel(String symbol,
                                    String companyName,
                                    String exchange,
                                    String industry,
                                    double marketCap,
                                    double currentPrice,
                                    double open,
                                    double high,
                                    double low,
                                    double previousClose) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.exchange = exchange;
        this.industry = industry;
        this.marketCap = marketCap;
        this.currentPrice = currentPrice;
        this.open = open;
        this.high = high;
        this.low = low;
        this.previousClose = previousClose;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getExchange() {
        return exchange;
    }

    public String getIndustry() {
        return industry;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getPreviousClose() {
        return previousClose;
    }
}
