package use_case.submit_application;

import entity.User;

public interface SubmitUserDataAccessInterface {

    boolean existsByName(String username);

    User get(String username);
}
