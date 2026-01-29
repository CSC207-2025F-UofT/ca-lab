package entity;

public class MarketStatus {
    private final String exchange;   // e.g. "US"
    private final boolean open;      // true if market is open
    private final String session;    // "pre", "regular", "post"
    private final String holiday;    // holiday name if closed, else ""
    private final long timestamp;    // UNIX seconds
    private final String timezone;   // e.g. "America/New_York"

    public MarketStatus(String exchange,
                        boolean open,
                        String session,
                        String holiday,
                        long timestamp,
                        String timezone) {
        this.exchange = exchange;
        this.open = open;
        this.session = session;
        this.holiday = holiday;
        this.timestamp = timestamp;
        this.timezone = timezone;
    }

    public String getExchange() {return exchange;}

    public boolean isOpen() {return open;}

    public String getSession() {return session;}

    public String getHoliday() {return holiday;}

    public long getTimestamp() {return timestamp;}

    public String getTimezone() {return timezone;}
}
