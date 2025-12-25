package interface_adapter.browse_filter;

import use_case.browse_filter.*;

public class BrowseFilterController {
    private final BrowseFilterInputBoundary interactor;

    public BrowseFilterController(BrowseFilterInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String type, String gender) {
        BrowseFilterInputData inputData = new BrowseFilterInputData(type, gender);
        interactor.execute(inputData);
    }
}
