package use_case.sharedsong;

/**
 * The output boundary for the Check Shared Song Use Case.
 */

public interface SharedSongOutputBoundary {
    /**
     * Prepares success view
     * @param outputData map in the form <User, yes||no>
     */
    void prepareSuccessView(SharedSongOutputData outputData);
    /**
     * Prepares failure view
     * @param errorMessage explanation for failure
     */
    void prepareFailureView(String errorMessage);
}
