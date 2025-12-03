package interface_adapter.user_profile;

import use_case.user_profile.UserProfileInputBoundary;
import use_case.user_profile.UserProfileInputData;

public class UserProfileController {

    private final UserProfileInputBoundary userProfileUseCaseInteractor;

    public UserProfileController(UserProfileInputBoundary userProfileUseCaseInteractor) {
        this.userProfileUseCaseInteractor = userProfileUseCaseInteractor;
    }

    /**
     * Executes the User Profile Use Case.
     * @param username current username
     * @param password current password
     * @param newUsername new username (null or empty if not changing)
     * @param newPassword new password (null or empty if not changing)
     * @param score user's score
     * @param bio the bio
     * @param fav_pokemon the favourite pokemon
     * @param name the display name
     * @param profilePhotoPath path to profile photo
     * @param bannerPath path to banner image
     */
    public void execute(String username, String password, String newUsername, String newPassword,
                       int score, String bio, String fav_pokemon, String name, 
                       String profilePhotoPath, String bannerPath) {
        final UserProfileInputData userProfileInputData = new UserProfileInputData(
                username, password, newUsername, newPassword, score, bio, fav_pokemon, 
                name, profilePhotoPath, bannerPath);

        userProfileUseCaseInteractor.execute(userProfileInputData);
    }
}

