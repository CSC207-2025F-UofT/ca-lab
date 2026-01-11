package interface_adapter.create_group;

import entity.SpotifyUser;
import use_case.create_group.CreateGroupInputBoundary;
import use_case.create_group.CreateGroupInputData;
import use_case.create_group.CreateGroupOutputBoundary;
import use_case.create_group.CreateGroupOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for create group use case
 */
public class CreateGroupController {

    private final CreateGroupInputBoundary createGroupUseCase;
    private final CreateGroupOutputBoundary presenter;

    /**
     * Constructor for CreateGroupController.
     *
     * @param createGroupUseCase the input boundary for the Create Group use case
     * @param presenter the presenter to handle output
     */
    public CreateGroupController(CreateGroupInputBoundary createGroupUseCase, CreateGroupOutputBoundary presenter) {
        this.createGroupUseCase = createGroupUseCase;
        this.presenter = presenter;
    }

    /**
     * Creates a group using the provided user input.
     *
     * @param group_name     the name of the group
     * @param spotifyUser    the user creating the group
     * @param initialMembers list of initial members
     */
    public void execute(String group_name, SpotifyUser spotifyUser, List<SpotifyUser> initialMembers) {
        // Build full member list: owner + additional members
        List<SpotifyUser> allMembers = new ArrayList<>();
        if (spotifyUser != null) {
            allMembers.add(spotifyUser);
        }
        if (initialMembers != null) {
            allMembers.addAll(initialMembers);
        }

        CreateGroupInputData inputData = new CreateGroupInputData(group_name, spotifyUser, allMembers);
        CreateGroupOutputData outputData = createGroupUseCase.execute(inputData);

        // Let the presenter update the view models and switch view
        presenter.present(outputData);
    }
}
