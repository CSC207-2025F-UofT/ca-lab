package use_case.create_group;

import entity.Group;

/**
 * DAO interface for the Create Group and Join Group Use Cases.
 */
public interface GroupDataAccessInterface {

    /**
     * Checks if a group with given group name already exists.
     * @param group_name to check for it's existence
     * @return true if a group with the given name does exist, false if not
     */
    boolean existsByName(String group_name);
    /**
     * Saves newly created Group entity in storage, used by create_group use case
     * @param group the Group entity to save */
    void saveGroup (Group group);

    /**
     * Retrieves a group by its group_name, or returns null if no such group exists
     * @param group_name trying to be retrieved
     * @return Group entity or null if no such group exists
     */
    Group getGroupByName(String group_name);

    /**
     * Updates existing Group entity in storage.
     *
     * Used to update group members in join_group use case
     * @param group the updated Group Entity to save
     */
    void updateGroup(Group group);


}

