package interface_adapter.browse_filter;

import interface_adapter.ViewManagerModel;

public class BrowseFilterPageController {

    private final ViewManagerModel viewManagerModel;
    private final BrowseFilterViewModel browseFilterViewModel;

    public BrowseFilterPageController(ViewManagerModel viewManagerModel,
                                      BrowseFilterViewModel browseFilterViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.browseFilterViewModel = browseFilterViewModel;
    }

    public void execute() {
        viewManagerModel.setState(browseFilterViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
