package use_case.get_topItems;

import java.util.List;

public class GetTopItemsOutputData {

    private final List<String> topItems;

    public GetTopItemsOutputData(List<String> topItems) {
        this.topItems = topItems;
    }

    public List<String> getTopItems() {
        return topItems;
    }
}
