package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;

/**
 * Leaderboard DAO.
 */
public interface UserListDataAccessInterface {

    // Get an ArrayList of all user objects in the local file.
    ArrayList<User> getUserList();

}
