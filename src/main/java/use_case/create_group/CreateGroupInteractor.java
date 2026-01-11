package use_case.create_group;

import data_access.GroupDataAccessObject;
import entity.Group;
import entity.SpotifyUser;
import use_case.GroupDataAccessInterface;

import java.util.List;

/**
 * Interactor for the Create Group use case.
 * Implements the input boundary.
 * Creates a group and persists it immediately.
 */
public class  CreateGroupInteractor implements CreateGroupInputBoundary {

    private final GroupDataAccessInterface groupDAO;

    // Constructor takes a GroupDataAccessInterface for persistence
    public CreateGroupInteractor(GroupDataAccessInterface groupDAO) {
        this.groupDAO = groupDAO;
    }

    @Override
    public CreateGroupOutputData execute(CreateGroupInputData inputData) {

        // Create the group with initial users
        List<SpotifyUser> initialUsers = inputData.getUsers();
        Group group = new Group(inputData.getGroup_name(), initialUsers);

        // Persist the group
        groupDAO.saveGroup(group);

        // Return output data
        return new CreateGroupOutputData(
                group.getGroup_name(),
                group.getUsers(),
                group,
                group.getGroupCode()
        );
    }
}