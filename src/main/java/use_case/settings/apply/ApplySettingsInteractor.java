
package use_case.settings.apply;

/**
 * Interactor for the apply settings class.
 */
public class ApplySettingsInteractor implements ApplySettingsInputBoundary {
    private final ApplySettingsOutputBoundary presenter;

    public ApplySettingsInteractor(ApplySettingsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    /**
     * Executes the apply settings use case.
     *
     * @param inputData the user's colour theme to save
     */
    @Override
    public void execute(ApplySettingsInputData inputData) {

        String theme = inputData.getTheme();

        if (theme == null || theme.isEmpty()) {
            presenter.prepareFailView("No theme was selected.");
        }

        else {
            ApplySettingsOutputData outputData = new ApplySettingsOutputData(theme);
            presenter.prepareSuccessView(outputData);
        }
    }
}
