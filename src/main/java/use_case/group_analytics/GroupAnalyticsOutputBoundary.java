package use_case.group_analytics;

public interface GroupAnalyticsOutputBoundary {
    void prepareSuccessView(GroupAnalyticsOutputData data);
    void prepareFailView(String error);
}