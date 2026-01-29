package interface_adapter.sharedsong;

import interface_adapter.ViewManagerModel;
import use_case.sharedsong.SharedSongOutputBoundary;
import use_case.sharedsong.SharedSongOutputData;
import view.SharedSongView;

/**
 * Presenter for the Shared Song Use Case.
 */
public class SharedSongPresenter implements SharedSongOutputBoundary {

    private final SharedSongViewModel sharedSongViewModel;
    private final ViewManagerModel viewManagerModel;


    public SharedSongPresenter(SharedSongViewModel sharedSongViewModel,
                               ViewManagerModel viewManagerModel) {
        this.sharedSongViewModel = sharedSongViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareSuccessView(SharedSongOutputData response) {
        SharedSongState state = sharedSongViewModel.getState();
        state.setUsernameToShared(response.getSharedSongOutputData());
        state.setErrorMessage("");

        sharedSongViewModel.setState(state);
        sharedSongViewModel.firePropertyChange("shared song");

        viewManagerModel.setState(sharedSongViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
    public void prepareFailureView(String errorMessage) {
        SharedSongState state = new SharedSongState();
        state.setErrorMessage(errorMessage);

        sharedSongViewModel.setState(state);
        sharedSongViewModel.firePropertyChange("error");
    }

}
