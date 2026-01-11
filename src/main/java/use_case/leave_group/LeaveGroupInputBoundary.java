package use_case.leave_group;
/**
 * Input Boundary for the Leave Group Use Case.
 */
public interface LeaveGroupInputBoundary {

    /**
     * executes Leave Group Use Case. After this executes the user will not have a group.
     * @param inputData the input data for leaving a group
     */
    void execute(LeaveGroupInputData inputData);
}
