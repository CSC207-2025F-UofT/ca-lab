package interface_adapter.sharedsong;

import entity.Group;
import entity.SpotifyUser;
import use_case.sharedsong.SharedSongInputBoundary;
import use_case.sharedsong.SharedSongInputData;

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
     * @param user the user checking if their song is shared
     * @param group the group the user is in
     */

    public void execute(SpotifyUser user, Group group) {
        final SharedSongInputData inputData = new SharedSongInputData(user,group);
        sharedSongUseCaseInteractor.execute(inputData);
    }
}
