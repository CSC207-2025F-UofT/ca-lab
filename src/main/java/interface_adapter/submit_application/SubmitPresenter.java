package interface_adapter.submit_application;

import interface_adapter.ViewManagerModel;
import interface_adapter.browse_filter.BrowseFilterViewModel;
import use_case.submit_application.SubmitOutputBoundary;
import use_case.submit_application.SubmitOutputData;

public class SubmitPresenter implements SubmitOutputBoundary {
    private final SubmitViewModel submitViewModel;
    private final BrowseFilterViewModel browseFilterViewModel;
    private final ViewManagerModel viewManagerModel;

    public SubmitPresenter(ViewManagerModel viewManagerModel,
                           SubmitViewModel submitViewModel,
                           BrowseFilterViewModel browseFilterViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.submitViewModel = submitViewModel;
        this.browseFilterViewModel = browseFilterViewModel;
    }

    @Override
    public void prepareSuccessView(String successMessage) {
        //show "submit successfully" to user
        final SubmitState submitState = submitViewModel.getState();
        submitState.setSuccess(successMessage);
        submitViewModel.firePropertyChange();
        //On success, switch back to BrowserFilter view
        viewManagerModel.setState(browseFilterViewModel.getViewName());
        viewManagerModel.firePropertyChange();
        submitViewModel.resetState();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        //username error or info not completed
        final SubmitState submitState = submitViewModel.getState();
        submitState.setError(errorMessage);
        submitViewModel.firePropertyChange();
    }

    @Override
    public void prepareAutofillView(SubmitOutputData submitOutputData) {
        final SubmitState submitState = submitViewModel.getState();
        submitState.setUsername(submitOutputData.getUsername());
        submitState.setFirstname(submitOutputData.getFirstname());
        submitState.setLastname(submitOutputData.getLastname());
        submitState.setAge(submitOutputData.getAge());
        submitState.setOccupation(submitOutputData.getOccupation());
        submitState.setAddress(submitOutputData.getAddress());
        submitState.setHomeEvi(submitOutputData.getHomeEvi());
        submitState.setTel(submitOutputData.getTel());
        submitState.setEmail(submitOutputData.getEmail());

        submitViewModel.setState(submitState);
        submitViewModel.firePropertyChange();
    }

    @Override
    public void switchToBrowserFilterView() {
        //for the cancel button
        viewManagerModel.setState(browseFilterViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
