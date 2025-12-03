package use_case.settings;

import org.junit.jupiter.api.Test;
import use_case.settings.apply.ApplySettingsInputData;
import use_case.settings.apply.ApplySettingsInteractor;
import use_case.settings.apply.ApplySettingsOutputBoundary;
import use_case.settings.apply.ApplySettingsOutputData;

import static org.junit.jupiter.api.Assertions.*;

class ApplySettingsInteractorTest {

    @Test
    void successCase() {
        ApplySettingsOutputBoundary presenter = new ApplySettingsOutputBoundary() {

            @Override
            public void prepareSuccessView(ApplySettingsOutputData outputData) {
                assertEquals("Dark", outputData.getTheme());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not hit fail view for valid theme");
            }
        };

        ApplySettingsInteractor interactor = new ApplySettingsInteractor(presenter);
        interactor.execute(new ApplySettingsInputData("Dark"));
    }

    @Test
    void failCase_noTheme() {
        ApplySettingsOutputBoundary presenter = new ApplySettingsOutputBoundary() {

            @Override
            public void prepareSuccessView(ApplySettingsOutputData outputData) {
                fail("Should not hit success for invalid theme");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No theme was selected.", error);
            }
        };

        ApplySettingsInteractor interactor = new ApplySettingsInteractor(presenter);
        interactor.execute(new ApplySettingsInputData("")); // invalid input
    }

    @Test
    void failCase_nullTheme() {
        ApplySettingsOutputBoundary presenter = new ApplySettingsOutputBoundary() {

            @Override
            public void prepareSuccessView(ApplySettingsOutputData outputData) {
                fail("Should not hit success for null theme");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No theme was selected.", error);
            }
        };

        ApplySettingsInteractor interactor = new ApplySettingsInteractor(presenter);
        interactor.execute(new ApplySettingsInputData(null)); // null input
    }
}
