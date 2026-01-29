package entity;

/**
 * Entity representing one earnings record returned by the API.
 */
public class EarningsRecord {
    private final String period;
    private final Double actual;
    private final Double estimate;
    private final Double surprise;

    public EarningsRecord(String period, Double actual, Double estimate, Double surprise) {
        this.period = period;
        this.actual = actual;
        this.estimate = estimate;
        this.surprise = surprise;
    }

    public String getPeriod() {
        return period;
    }

    public Double getActual() {
        return actual;
    }

    public Double getEstimate() {
        return estimate;
    }

    public Double getSurprise() {
        return surprise;
    }
}
