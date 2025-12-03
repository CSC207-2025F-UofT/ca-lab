package use_case.user_profile;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;
import view.GameDashboard;

/**
 * The interactor for the User Profile use case.
 */
public class UserProfileInteractor implements UserProfileInputBoundary {

    private final UserProfileUserDataAccessInterface userDataAccessObject;
    private final UserProfileOutputBoundary userPresenter;
    private final UserFactory userFactory;
    private GameDashboard dashboard;

    public UserProfileInteractor(UserProfileUserDataAccessInterface userDataAccessObject,
            UserProfileOutputBoundary userPresenter,
            UserFactory userFactory, GameDashboard dashboard) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = userPresenter;
        this.userFactory = userFactory;
        this.dashboard = dashboard;
    }

    private static final int MAX_BIO_LENGTH = 500;

    /**
     * Executes the User Profile use case.
     *
     * @param userProfileInputData the input data for this use case
     */
    @Override
    public void execute(UserProfileInputData userProfileInputData) {
        String currentUsername = dashboard.getCurrentUser();

        User u = ((FileUserDataAccessObject)userDataAccessObject).get(currentUsername);
        
        // Validate display name
        if (userProfileInputData.getName() == null || userProfileInputData.getName().trim().isEmpty()) {
            userPresenter.prepareFailView("Display name cannot be empty.");
            return;
        }

        if (userProfileInputData.getName().length() > 32) {
            userPresenter.prepareFailView("Display name must be <= 32 characters long.");
            return;
        }

        // Validate bio length
        if (userProfileInputData.getBio() != null && userProfileInputData.getBio().length() > MAX_BIO_LENGTH) {
            userPresenter.prepareFailView("Bio must be <= " + MAX_BIO_LENGTH + " characters long.");
            return;
        }

        // Validate new username if provided
        String newUsername = userProfileInputData.getNewUsername();
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            if (newUsername.length() > 32) {
                userPresenter.prepareFailView("Username must be <= 32 characters long.");
                return;
            }
            if (!newUsername.equals(currentUsername) && userDataAccessObject.existsByName(newUsername)) {
                userPresenter.prepareFailView("Username already exists.");
                return;
            }
        }

        // Validate new password if provided
        String newPassword = userProfileInputData.getNewPassword();
        if (newPassword != null && newPassword.trim().isEmpty()) {
            userPresenter.prepareFailView("Password cannot be empty.");
            return;
        }

        // Determine final username and password
        String finalUsername = (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(currentUsername)) 
            ? newUsername 
            : currentUsername;
        String finalPassword = (newPassword != null && !newPassword.trim().isEmpty()) 
            ? newPassword 
            : u.getPassword();

        // Create user with updated information
        final User user = userFactory.create(
                finalUsername, // Use new username if changed, otherwise keep current
                finalPassword, // Use new password if provided, otherwise keep current
                u.getScore(),
                userProfileInputData.getBio(),
                userProfileInputData.getFav_pokemon(),
                userProfileInputData.getProfilePhotoPath(),
                userProfileInputData.getBannerPath()
        );
        // Set the display name
        user.setName(userProfileInputData.getName());

        // Update username if changed
        if (!finalUsername.equals(currentUsername)) {
            userDataAccessObject.updateUsername(currentUsername, finalUsername, user);
            // Update dashboard's current user
            dashboard.setUser(finalUsername);
        } else {
            // Use the current username as the key when updating profile
            userDataAccessObject.updateUserProfile(finalUsername, user);
        }

        final UserProfileOutputData userProfileOutputData = new UserProfileOutputData(
                finalUsername, 
                user.getName(), // Display name
                user.getBio(), 
                user.getFavPokemon(),
                user.getProfilePhotoPath(),
                user.getBannerPath());
        userPresenter.prepareSuccessView(userProfileOutputData);
    }
}
