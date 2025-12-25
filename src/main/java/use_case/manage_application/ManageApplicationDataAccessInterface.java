package use_case.manage_application;
import entity.Application;
import java.util.List;

/**
 * Input Boundary for actions which are related to logging in.
 */
public interface ManageApplicationDataAccessInterface {

    // Get all applications
    List<Application> getAllApplications();

    // Get an application by id
    Application getApplicationById(String id);

    // Update an application's status
    void updateStatus(Application application, String status);

}
