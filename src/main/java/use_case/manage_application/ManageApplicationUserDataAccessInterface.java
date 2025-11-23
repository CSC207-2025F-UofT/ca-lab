package use_case.manage_application;

import use_case.login.LoginInputData;

/**
 * Input Boundary for actions which are related to logging in.
 */
public interface ManageApplicationUserDataAccessInterface {

    /**
     * Executes the login use case.
     * @param loginInputData the input data
     */
    void execute(LoginInputData loginInputData);
}
