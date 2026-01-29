package use_case.manage_application;

/**
 * Input Boundary for actions which are related to logging in.
 */
public interface ManageApplicationOutputBoundary {
    // note that this is an interface so there isn't implementation of methods

    /**
     * Prepares the success view for the Manage Application Use Case
     * @param outputData the output data
     */
    void present(ManageApplicationOutputData outputData);
}
