package data_access;

import entity.Application;
import use_case.submit_application.SubmitApplicationDataAccessInterface;
import use_case.manage_application.ManageApplicationDataAccessInterface;
import java.sql.*;
import java.util.*;

public class DBApplicationDataAccessObject implements SubmitApplicationDataAccessInterface, ManageApplicationDataAccessInterface {
    // This class needs to implement two data access interface because both use cases handles the same type of data

    private final DatabaseApplicationGateway gateway;

    // Need to give the DAO a gateway to work with
    public DBApplicationDataAccessObject(DatabaseApplicationGateway gateway) {
        this.gateway=gateway;
    }

    @Override
    public void save(Application application) {
        gateway.addApplication(application);
    }

    @Override
    // Get all applications
    public List<Application> getAllApplications() {
        return gateway.fetchAllApplications();
    };

    @Override
    // Get an application by id
    public Application getApplicationById(String id) {
        return gateway.fetchApplicationById(id);
    }

    @Override
    // Update an application's status
    public void updateStatus(Application application, String status) {
        gateway.updateApplicationStatus(application, status);
    };

}
