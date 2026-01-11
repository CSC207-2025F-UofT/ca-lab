package interface_adapter.sharedsong;

import entity.SpotifyUser;
import use_case.sharedsong.SharedSongInputBoundary;
import use_case.sharedsong.SharedSongInputData;

import java.util.List;

/**
 * Controller for the Shared Song Use Case.
 */

public class SharedSongController {

    private final SharedSongInputBoundary sharedSongUseCaseInteractor;

    public SharedSongController(SharedSongInputBoundary sharedSongUseCaseInteractor) {
        this.sharedSongUseCaseInteractor = sharedSongUseCaseInteractor;
    }

    /**
     * Executes the Shared Song Use Case.
     *
     * @param user       the user checking if their song is shared
     * @param groupUsers the list of users in the group the user is in
     */

    public void execute(SpotifyUser user, List<SpotifyUser> groupUsers) {
        final SharedSongInputData inputData = new SharedSongInputData(user, groupUsers);
        sharedSongUseCaseInteractor.execute(inputData);
    }
}
