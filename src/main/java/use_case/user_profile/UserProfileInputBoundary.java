package use_case.user_profile;

/**
 * The User Profile Use Case Input Boundary.
 */
public interface UserProfileInputBoundary {

    /**
     * Execute the User Profile Use Case.
     * @param userProfileInputData the input data for this use case
     */
    void execute(UserProfileInputData userProfileInputData);
}

