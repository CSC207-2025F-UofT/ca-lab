package use_case.submit_application;

import entity.Application;
import entity.User;

public interface SubmitApplicationDataAccessInterface {

    void save(Application application);

}
