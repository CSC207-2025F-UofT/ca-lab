package data_access;

import entity.Application;
import use_case.manage_application.ManageApplicationDataAccessInterface;
import use_case.submit_application.SubmitApplicationDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryApplicationDataAccessObject
        implements SubmitApplicationDataAccessInterface, ManageApplicationDataAccessInterface {
    private Map<String, Application> applications = new HashMap<>();

    @Override
    public List<Application> getAllApplications() {
        return new ArrayList<>(applications.values());
    }

    @Override
    public Application getApplicationById(String id) {
        return applications.get(id);
    }

    @Override
    public void updateStatus(Application application, String status) {
        Application existing = applications.get(application.getApplicationId());
        if (existing != null) {
            existing.setStatus(status);
        }
    }

    @Override
    public void save(Application application) {
        applications.put(application.getApplicationId(), application);
    }
}
