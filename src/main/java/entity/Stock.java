package entity;

public class Stock {
    private final String mic;
    private final String type;
    private final String currency;
    private final String description;
    private final String displaySymbol;
    private final String figi;
    private final String symbol;

    public Stock(String currency, String description, String displaySymbol,
                                                          String figi, String mic, String symbol, String type) {
        this.currency = currency;
        this.description = description;
        this.displaySymbol = displaySymbol;
        this.mic = mic;
        this.type = type;
        this.symbol  = symbol;
        this.figi = figi;

    }

    public String getMic() {
        return mic;
    }

    public String getType() {
        return type;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public String getFigi() {
        return figi;
    }

    public String getSymbol() {
        return symbol;
    }
}
