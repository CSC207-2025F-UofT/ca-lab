package use_case.create_group;

import entity.Group;
import entity.SpotifyUser;
import java.util.List;

/**
 * Interactor for the Create Group use case.
 * Implements the input boundary.
 */
public abstract class CreateGroupInteractor implements CreateGroupInputBoundary {

    @Override
    public CreateGroupOutputData execute(CreateGroupInputData inputData) {

        Group group = new Group(inputData.getGroup_name());

        List<SpotifyUser> initialMembers = inputData.getInitialMembers();
        if (initialMembers != null) {
            for (SpotifyUser user : initialMembers) {
                group.addUser(user);
            }
        }

        // Return output data
        return new CreateGroupOutputData(
                group.getGroup_name(),
                group.getUsers()
        );
    }
}