package use_case.back;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BackInteractorTest {

    @Test
    void testAccessSettingsCallsPresenter() {
        // Arrange
        final boolean[] wasCalled = {false};

        BackOutputBoundary presenter = new BackOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                wasCalled[0] = true;
            }
        };

        BackInteractor interactor = new BackInteractor(presenter);

        // Act
        interactor.execute();

        // Assert
        assertTrue(wasCalled[0], "Presenter should be called");
    }
}
