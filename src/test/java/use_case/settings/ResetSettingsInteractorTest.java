package use_case.settings;

import org.junit.jupiter.api.Test;
import use_case.settings.reset.ResetSettingsInteractor;
import use_case.settings.reset.ResetSettingsOutputBoundary;
import use_case.settings.reset.ResetSettingsOutputData;

import static org.junit.jupiter.api.Assertions.*;

class ResetSettingsInteractorTest {
    @Test
    void successCase() {
        ResetSettingsOutputBoundary presenter = new ResetSettingsOutputBoundary() {
            @Override
            public void prepareSuccessView(ResetSettingsOutputData data) {
                assertNotNull(data);
                assertEquals("Light", data.getDefaultTheme());
            }
        };

        ResetSettingsInteractor interactor = new ResetSettingsInteractor(presenter);
        interactor.execute();
    }
}

