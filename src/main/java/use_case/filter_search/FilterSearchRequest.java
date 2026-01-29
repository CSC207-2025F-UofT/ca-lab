package use_case.filter_search;

public class FilterSearchRequest {
    private final String exchange;
    private final String mic;
    private final String securityType;
    private final String currency;

    public  FilterSearchRequest(String exchange) {
        this.exchange = exchange;
        this.mic = null;
        this.securityType = null;
        this.currency = null;
    }

    public FilterSearchRequest(String exchange, String mic, String securityType, String currency) {
        this.exchange = exchange;
        this.mic = mic;
        this.securityType = securityType;
        this.currency = currency;

    }

    public String getExchange() {return this.exchange;}

    public String getMic() {return this.mic;}

    public String getSecurityType() {return this.securityType;}

    public String getCurrency() {return this.currency;}

}
