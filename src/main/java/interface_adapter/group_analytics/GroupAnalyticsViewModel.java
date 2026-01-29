package interface_adapter.group_analytics;

import interface_adapter.ViewModel;

public class GroupAnalyticsViewModel extends ViewModel<GroupAnalyticsState> {

    public GroupAnalyticsViewModel() {
        super("group analytics");
        setState(new GroupAnalyticsState());
    }
}
