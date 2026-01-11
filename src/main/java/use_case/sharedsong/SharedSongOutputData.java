package use_case.sharedsong;

import java.util.Map;

/**
 * The output data for the Check Shared Song Use Case.
 */
public class SharedSongOutputData {
    // Maps username to either Yes or No
    private final Map<String, String> sharedSongOutputData;

    public SharedSongOutputData(Map<String, String> sharedSongOutputData) {
        this.sharedSongOutputData = sharedSongOutputData;
    }

    public Map<String, String> getSharedSongOutputData() {
        return sharedSongOutputData;
    }
}
