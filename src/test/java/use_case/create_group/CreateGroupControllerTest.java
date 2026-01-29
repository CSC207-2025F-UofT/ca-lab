package use_case.create_group;
import interface_adapter.create_group.CreateGroupController;
import org.junit.jupiter.api.Test;
import use_case.create_group.*;

import entity.SpotifyUser;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGroupControllerTest {

    static class DummyInteractor implements CreateGroupInputBoundary {
        CreateGroupInputData received;

        @Override
        public CreateGroupOutputData execute(CreateGroupInputData inputData) {
            this.received = inputData;
            return new CreateGroupOutputData(
                    inputData.getGroup_name(),
                    inputData.getUsers(),
                    null
            );
        }
    }

    static class DummyPresenter implements CreateGroupOutputBoundary {
        CreateGroupOutputData received;

        @Override
        public void present(CreateGroupOutputData output) {
            this.received = output;
        }
    }

    @Test
    void testControllerBuildsInputCorrectly() {
        DummyInteractor interactor = new DummyInteractor();
        DummyPresenter presenter = new DummyPresenter();

        CreateGroupController controller = new CreateGroupController(interactor, presenter);

        SpotifyUser owner = new SpotifyUser("Sam", "A","R","ID");

        controller.execute("GroupX", owner, List.of(owner));

        assertNotNull(interactor.received);
        assertEquals("GroupX", interactor.received.getGroup_name());
        assertEquals(2, interactor.received.getUsers().size()); // owner is added twice intentionally
    }
}