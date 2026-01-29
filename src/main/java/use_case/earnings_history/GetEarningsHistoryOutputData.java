package use_case.earnings_history;

import entity.EarningsRecord;

import java.util.List;

public class GetEarningsHistoryOutputData {

    private final String symbol;
    private final List<EarningsRecord> records;

    public GetEarningsHistoryOutputData(String symbol, List<EarningsRecord> records) {
        this.symbol = symbol;
        this.records = records;
    }

    public String getSymbol() {
        return symbol;
    }

    public List<EarningsRecord> getRecords() {
        return records;
    }
}
