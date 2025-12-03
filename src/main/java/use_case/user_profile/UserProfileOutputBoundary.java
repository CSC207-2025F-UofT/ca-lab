package use_case.user_profile;

/**
 * The User Profile Use Case Output Boundary.
 */
public interface UserProfileOutputBoundary {

    /**
     * Prepare the success view with the updated user profile data.
     * @param userProfileOutputData the output data for this use case
     */
    void prepareSuccessView(UserProfileOutputData userProfileOutputData);

    /**
     * Prepare the fail view with an error message.
     * @param error the error message
     */
    void prepareFailView(String error);
}

