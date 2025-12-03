package use_case.user_profile;

import entity.User;

/**
 * Interface for data access operations related to user profile.
 */
public interface UserProfileUserDataAccessInterface {

    /**
     * Update the user profile.
     * 
     * @param username the username (key) for the user to update
     * @param user     the user with updated profile information
     */
    void updateUserProfile(String username, User user);

    /**
     * Get a user by username.
     *
     * @param username the username
     * @return the User object
     */
    User get(String username);

    /**
     * Check if a username already exists.
     * 
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByName(String username);

    /**
     * Update the username of a user. This involves removing the old entry and
     * creating a new one.
     * 
     * @param oldUsername the current username
     * @param newUsername the new username
     * @param user        the user object with updated information
     */
    void updateUsername(String oldUsername, String newUsername, User user);
}
