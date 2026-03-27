package entity;

import java.time.Instant;

public class Trade {
    private final String symbol;
    private final double price;
    private final double volume;
    private final Instant timestamp;

    public Trade(String symbol, double price, double volume, Instant timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getVolume() {
        return volume;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

