package use_case.sharedsong;

import data_access.SpotifyDataAccessObject;

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
