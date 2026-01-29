package interface_adapter.get_topItems;

import use_case.get_topItems.GetTopItemsOutputData;
import use_case.get_topItems.GetTopItemsOutputBoundary;

public class GetTopItemsPresenter implements GetTopItemsOutputBoundary {

    private final GetTopItemsViewModel GetTopItemsViewModel;

    public GetTopItemsPresenter(GetTopItemsViewModel GetTopItemsViewModel) {
        this.GetTopItemsViewModel = GetTopItemsViewModel;
    }

    @Override
    public void prepareSuccessView(GetTopItemsOutputData response) {
        final GetTopItemsState getTopItemsState = GetTopItemsViewModel.getState();
        getTopItemsState.setSuccess(true);
        getTopItemsState.setTopItems(response.getTopItems());

        GetTopItemsViewModel.setState(getTopItemsState);
        GetTopItemsViewModel.firePropertyChange();

    }

    public void prepareFailureView(String errorMessage) {
        final GetTopItemsState getTopItemsState = GetTopItemsViewModel.getState();
        getTopItemsState.setSuccess(false);
        getTopItemsState.setTopItems(null);

        GetTopItemsViewModel.setState(getTopItemsState);
        GetTopItemsViewModel.firePropertyChange();
    }
}
