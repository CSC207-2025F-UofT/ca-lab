package interface_adapter.market_status;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MarketStatusViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // What is shown in UI
    private String statusText;

    // Whether the market is open
    private boolean open;

    // Extra info, optional
    private String session;   // "pre", "regular", "post", etc.
    private String exchange;  // e.g. "US"
    private String holiday;   // holiday name, or ""
    private String timezone;  // e.g. "America/New_York";

    private String errorMessage;

    // --- observer methods ---
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this);
    }

    // getters
    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
