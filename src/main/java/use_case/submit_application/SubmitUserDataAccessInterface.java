package use_case.submit_application;

import entity.User;
import entity.Visitor;

public interface SubmitUserDataAccessInterface {

    boolean existsByName(String username);

    User get(String username);

    String getCurrentUsername();

    void save(User user);
}
