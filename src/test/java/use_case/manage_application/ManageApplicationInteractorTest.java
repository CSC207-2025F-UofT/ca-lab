package use_case.manage_application;

import entity.Application;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageApplicationInteractorTest {

    // Fake DAO for testing
    static class FakeDAO implements ManageApplicationDataAccessInterface {
        List<Application> apps = new ArrayList<>();

        @Override
        public List<Application> getAllApplications() {
            return apps;
        }

        @Override
        public Application getApplicationById(String id) {
            return apps.stream().filter(a -> a.getApplicationId().equals(id)).findFirst().orElse(null);
        }

        @Override
        public void updateStatus(Application application, String status) {
            application.setStatus(status);
        }
    }

    // Fake Presenter for testing
    static class FakePresenter implements ManageApplicationOutputBoundary {
        ManageApplicationOutputData captured;

        @Override
        public void present(ManageApplicationOutputData outputData) {
            captured = outputData;
        }
    }

    @Test
    void execute_callsDAOAndPresenterCorrectly() {
        FakeDAO dao = new FakeDAO();
        FakePresenter presenter = new FakePresenter();

        Application app1 = new Application("eeee12394059", "pet123", "John", "Doe", "jd@mail.com",
                "123456", "30", "Engineer", "I love pets", "Big house", "Full time", "None");
        dao.apps.add(app1);

        ManageApplicationInteractor interactor = new ManageApplicationInteractor(dao, presenter);

        interactor.execute();

        // Verify presenter got the same applications
        assertNotNull(presenter.captured);
        assertEquals(1, presenter.captured.getApplications().size());

        Application capturedApp = presenter.captured.getApplications().get(0);

        assertEquals("eeee12394059", capturedApp.getApplicationId());
        assertEquals("John", capturedApp.getFirstName());
        assertEquals("Doe", capturedApp.getLastName());
        assertEquals("jd@mail.com", capturedApp.getEmail());
        assertEquals("123456", capturedApp.getPhoneNumber());
        assertEquals("30", capturedApp.getAge());
        assertEquals("Engineer", capturedApp.getOccupation());
        assertEquals("pet123", capturedApp.getPetId());
        assertEquals("I love pets", capturedApp.getReason());
        assertEquals("Big house", capturedApp.getHomeEnvironment());
        assertEquals("Full time", capturedApp.getAvailability());
        assertEquals("None", capturedApp.getPreviousExperience());
        assertEquals("processing", capturedApp.getStatus()); // default status

    }
}
