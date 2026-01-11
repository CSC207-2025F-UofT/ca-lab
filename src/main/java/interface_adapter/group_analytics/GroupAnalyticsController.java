package interface_adapter.group_analytics;

import entity.UserTasteProfile;
import use_case.group_analytics.GroupAnalyticsInputBoundary;
import use_case.group_analytics.GroupAnalyticsInputData;

import java.util.List;

public class GroupAnalyticsController {

    private final GroupAnalyticsInputBoundary interactor;

    public GroupAnalyticsController(GroupAnalyticsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void analyzeGroup(List<UserTasteProfile> profiles) {
        GroupAnalyticsInputData inputData = new GroupAnalyticsInputData(profiles);
        interactor.execute(inputData);
    }
}
