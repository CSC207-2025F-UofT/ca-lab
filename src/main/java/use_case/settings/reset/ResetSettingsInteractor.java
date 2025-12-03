package use_case.settings.reset;

/**
 * The interactor for the Reset Settings use case.
 */
public class ResetSettingsInteractor implements ResetSettingsInputBoundary {
    private final ResetSettingsOutputBoundary presenter;

    public ResetSettingsInteractor(ResetSettingsOutputBoundary presenter) {

        this.presenter = presenter;
    }

    /**
     * The use case sets it back to the default light theme.
     * There is no prepareFailView because there should not be an error that could interfere.
     */
    @Override
    public void execute() {

        // Default theme
        String defaultTheme = "Light";

        ResetSettingsOutputData outputData =
                new ResetSettingsOutputData(defaultTheme);

        presenter.prepareSuccessView(outputData);
    }
}
