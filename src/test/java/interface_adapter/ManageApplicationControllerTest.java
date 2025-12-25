package interface_adapter.manage_application;

import org.junit.jupiter.api.Test;
import use_case.manage_application.ManageApplicationInputBoundary;

import static org.junit.jupiter.api.Assertions.*;

class ManageApplicationControllerTest {

    @Test
    void execute_callsInteractor() {
        // Fake interactor to record calls
        class FakeInteractor implements ManageApplicationInputBoundary {
            boolean executed = false;
            @Override public void execute() { executed = true; }
        }

        FakeInteractor fake = new FakeInteractor();
        ManageApplicationController controller = new ManageApplicationController(fake);

        controller.execute();

        assertTrue(fake.executed);
    }
}
