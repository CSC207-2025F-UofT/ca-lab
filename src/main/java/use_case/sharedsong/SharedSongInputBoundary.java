package use_case.sharedsong;

/**
 * The Check Shared Song Use Case.
 */
public interface SharedSongInputBoundary {
    /**
     * Execute the Check Shared Song Use Case.
     * @param sharedSongInputData the input data for this use case
     */
    void execute(SharedSongInputData sharedSongInputData);
}
