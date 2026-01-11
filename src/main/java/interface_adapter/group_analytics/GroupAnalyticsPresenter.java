package interface_adapter.group_analytics;

import use_case.group_analytics.GroupAnalyticsOutputBoundary;
import use_case.group_analytics.GroupAnalyticsOutputData;

public class GroupAnalyticsPresenter implements GroupAnalyticsOutputBoundary {

    private final GroupAnalyticsViewModel viewModel;

    public GroupAnalyticsPresenter(GroupAnalyticsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GroupAnalyticsOutputData data) {
        GroupAnalyticsState state = viewModel.getState();
        state.setPairwise(data.getPairwise());
        state.setAverageSimilarity(data.getAverageSimilarity());
        state.setVibeScores(data.getVibeScores());
        state.setVerdict(data.getVerdict());
        state.setErrorMessage(null);

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        GroupAnalyticsState state = viewModel.getState();
        state.setErrorMessage(error);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}
