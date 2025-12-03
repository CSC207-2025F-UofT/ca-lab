package interface_adapter.back;

import use_case.back.BackInputBoundary;

/**
 * Controller responsible for navigating back to the dashboard
 * from the settings screen.
 */
public class BackController {
    private final BackInputBoundary interactor;

    public BackController(BackInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the use case to return to the dashboard.
     */
    public void execute() {
        interactor.execute();
    }
}
