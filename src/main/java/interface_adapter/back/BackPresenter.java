package interface_adapter.back;

import interface_adapter.ViewManagerModel;
import use_case.back.BackOutputBoundary;

/**
 * Presenter responsible for navigating from the settings screen
 * to a target view, in this case the dashboard.
 */
public class BackPresenter implements BackOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final String targetView;

    /**
     * Constructs an AccessSettingsPresenter.
     *
     * @param viewManagerModel  the view manager used to switch views
     * @param targetView        the name of the view to navigate to
     */
    public BackPresenter(ViewManagerModel viewManagerModel,
                         String targetView) {
        this.viewManagerModel = viewManagerModel;
        this.targetView = targetView;
    }

    /**
     * Prepares the success view by clearing any existing errors and switching
     * to the target view.
     */
    @Override
    public void prepareSuccessView() {
        // switch to the target view
        viewManagerModel.setState(targetView);
        viewManagerModel.firePropertyChange();
    }

}
