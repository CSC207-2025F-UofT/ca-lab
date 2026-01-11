package use_case.leave_group;

/**
 * The output boundary for the Leave Group Use Case
 */

public interface LeaveGroupOutputBoundary {

    /**
     * Prepares view for leaving group
     * @param outputData the output data
     */
    void present(LeaveGroupOutputData outputData);
}
