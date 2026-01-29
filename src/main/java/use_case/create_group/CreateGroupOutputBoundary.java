package use_case.create_group;

/**
 * Output Boundary for Create Group use case
 */
public interface CreateGroupOutputBoundary {
/**
 * Sends output data from interactor to presenter
 * @param outputData the output data containing the newly created group's information
 */
    void present(CreateGroupOutputData outputData);
}
