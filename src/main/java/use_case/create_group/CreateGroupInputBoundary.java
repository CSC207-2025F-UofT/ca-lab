package use_case.create_group;

/**
 * Input boundary for the Create Group use case.
 * The controller calls this interface.
 */

public interface CreateGroupInputBoundary {
    CreateGroupOutputData execute(CreateGroupInputData inputData);

    /**
     * executes create group use case
     * @param inputData the input data for creating a group
     */
}
