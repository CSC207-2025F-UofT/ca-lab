package use_case.user_profile;

/**
 * The input data for the User Profile Use Case.
 */
public class UserProfileInputData {

    private final String username;
    private final String password;
    private final String newUsername;
    private final String newPassword;
    private final int score;
    private final String bio;
    private final String fav_pokemon;
    private final String name;
    private final String profilePhotoPath;
    private final String bannerPath;

    public UserProfileInputData(String username, String password, String newUsername, String newPassword,
            int score, String bio, String fav_pokemon, String name,
            String profilePhotoPath, String bannerPath) {
        this.username = username;
        this.password = password;
        this.newUsername = newUsername;
        this.newPassword = newPassword;
        this.score = score;
        this.bio = bio;
        this.fav_pokemon = fav_pokemon;
        this.name = name;
        this.profilePhotoPath = profilePhotoPath;
        this.bannerPath = bannerPath;
    }

    String getNewUsername() {
        return newUsername;
    }

    String getNewPassword() {
        return newPassword;
    }

    String getBio() {
        return bio;
    }

    String getFav_pokemon() {
        return fav_pokemon;
    }

    String getName() {
        return name;
    }

    String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    String getBannerPath() {
        return bannerPath;
    }
}
