package interface_adapter.earnings_history;

import entity.EarningsRecord;

import java.util.ArrayList;
import java.util.List;

public class EarningsHistoryState {

    public enum MessageType { NONE, INFO, WARNING, ERROR }

    private String symbol = "";
    private List<EarningsRecord> records = new ArrayList<>();
    private String message = "";
    private MessageType messageType = MessageType.NONE;
    private boolean loading = false;

    public EarningsHistoryState() {
    }

    public EarningsHistoryState(EarningsHistoryState copy) {
        this.symbol = copy.symbol;
        this.records = new ArrayList<>(copy.records);
        this.message = copy.message;
        this.messageType = copy.messageType;
        this.loading = copy.loading;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<EarningsRecord> getRecords() {
        return records;
    }

    public void setRecords(List<EarningsRecord> records) {
        this.records = records;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessage(String message, MessageType type) {
        this.message = message;
        this.messageType = type;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
