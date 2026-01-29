package interface_adapter.create_group;
import entity.SpotifyUser;
import use_case.create_group.CreateGroupInputBoundary;
import use_case.create_group.CreateGroupInputData;

import java.util.List;

/**
 * Controller for create group use case
 */
public class CreateGroupController {

    private final CreateGroupInputBoundary createGroupUseCase;

    /**
     * Constructor for CreateGroupController.
     *
     * @param createGroupUseCase the input boundary for the Create Group use case
     */
    public CreateGroupController(CreateGroupInputBoundary createGroupUseCase) {
        this.createGroupUseCase = createGroupUseCase;
    }

    /**
     * Creates a group using the provided user input.
     *
     * @param group_name the name of the group
     * @param owner the user creating the group
     * @param initialMembers  list of initial members
     */
    public void create(String group_name, SpotifyUser owner, List<SpotifyUser> initialMembers) {
        CreateGroupInputData inputData = new CreateGroupInputData(group_name, owner, initialMembers);
        createGroupUseCase.execute(inputData);

    }
}
