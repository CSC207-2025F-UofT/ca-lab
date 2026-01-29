package interface_adapter.sharedsong;

import interface_adapter.ViewManagerModel;
import use_case.sharedsong.SharedSongOutputBoundary;
import use_case.sharedsong.SharedSongOutputData;

/**
 * Presenter for the Shared Song Use Case.
 */
public class SharedSongPresenter implements SharedSongOutputBoundary {

    // may be same as group tab view model
    private final CheckSharedSongViewModel sharedSongViewModel;
    private final CheckedSharedSongViewModel checkedSharedSongViewModel;
    // may be group view manager
    private final ViewManagerModel viewManagerModel;

    public SharedSongPresenter(ViewManagerModel viewManagerModel,
                               CheckSharedSongViewModel sharedSongViewModel,
                               CheckedSharedSongViewModel checkedSharedSongViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.sharedSongViewModel = sharedSongViewModel;
        this.checkedSharedSongViewModel = checkedSharedSongViewModel;
    }

    public void prepareSuccessView(SharedSongOutputData response) {
        // on success, display song and who has saved it
    }
    public void prepareFailureView(String errorMessage) {
        // on failure, display error message
    }
}
