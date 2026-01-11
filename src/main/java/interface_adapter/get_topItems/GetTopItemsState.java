package interface_adapter.get_topItems;

import use_case.get_topItems.TimeRange;
import use_case.get_topItems.TopItem;

import java.util.*;

public class GetTopItemsState {
    private List<String> topItems = new ArrayList<>();
    private boolean success = false;
    private TimeRange selectedTime = null;
    private TopItem selectedTopItem = null;

    public List<String> getTopItems() {
        return topItems;
    }
    public boolean getSuccess() {
        return success;
    }
    public TimeRange getSelectedTime() { return selectedTime; }
    public TopItem getSelectedTopItem() { return selectedTopItem; }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setTopItems(List<String> topItems) {
        this.topItems = topItems;
    }
    public void setSelectedTime(TimeRange selectedTime) { this.selectedTime = selectedTime; }
    public void setSelectedTopItem(TopItem selectedTopItem) { this.selectedTopItem = selectedTopItem; }
}
