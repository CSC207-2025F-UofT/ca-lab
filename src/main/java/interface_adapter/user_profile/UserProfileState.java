package interface_adapter.user_profile;

import entity.User;

public class UserProfileState {

    private String username = "";
    private String password = "";
    private int score = 0;
    private String bio = "";
    private String fav_pokemon = "";
    private String name = "";
    private String profilePhotoPath = "";
    private String bannerPath = "";
    private String profileError;
    private int bioCharacterCount = 0;
    private int profileCompletionPercentage = 0;

    public UserProfileState(UserProfileState copy) {
        username = copy.username;
        password = copy.password;
        score = copy.score;
        bio = copy.bio;
        fav_pokemon = copy.fav_pokemon;
        name = copy.name;
        profilePhotoPath = copy.profilePhotoPath;
        bannerPath = copy.bannerPath;
        profileError = copy.profileError;
        bioCharacterCount = copy.bioCharacterCount;
        profileCompletionPercentage = copy.profileCompletionPercentage;
    }

    public UserProfileState(User u) {
        if (u != null) {
            username = u.getName();
            password = u.getPassword();
            score = u.getScore();
            bio = u.getBio();
            fav_pokemon = u.getFavPokemon();
            name = u.getName();
            profilePhotoPath = u.getProfilePhotoPath() != null ? u.getProfilePhotoPath() : "";
            bannerPath = u.getBannerPath() != null ? u.getBannerPath() : "";
            bioCharacterCount = bio != null ? bio.length() : 0;
            profileCompletionPercentage = calculateCompletionPercentage(u);
        }
    }

    private int calculateCompletionPercentage(User u) {
        int completedFields = 0;
        int totalFields = 5; // name, bio, fav_pokemon, profilePhotoPath, bannerPath

        if (u.getName() != null && !u.getName().trim().isEmpty())
            completedFields++;
        if (u.getBio() != null && !u.getBio().trim().isEmpty())
            completedFields++;
        if (u.getFavPokemon() != null && !u.getFavPokemon().isEmpty() && !u.getFavPokemon().equals("None"))
            completedFields++;
        if (u.getProfilePhotoPath() != null && !u.getProfilePhotoPath().isEmpty())
            completedFields++;
        if (u.getBannerPath() != null && !u.getBannerPath().isEmpty())
            completedFields++;

        return (int) Math.round((completedFields * 100.0) / totalFields);
    }

    public UserProfileState() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setBio(String bio) {
        this.bio = bio;
        this.bioCharacterCount = bio != null ? bio.length() : 0;
        updateCompletionPercentage();
    }

    public String getBio() {
        return bio;
    }

    public int getBioCharacterCount() {
        return bioCharacterCount;
    }

    public void setBioCharacterCount(int count) {
        this.bioCharacterCount = count;
    }

    public int getProfileCompletionPercentage() {
        return profileCompletionPercentage;
    }

    public void setProfileCompletionPercentage(int percentage) {
        this.profileCompletionPercentage = percentage;
    }

    public void setFav_pokemon(String fav_pokemon) {
        this.fav_pokemon = fav_pokemon;
        updateCompletionPercentage();
    }

    public String getFav_pokemon() {
        return fav_pokemon;
    }

    public void setName(String name) {
        this.name = name;
        updateCompletionPercentage();
    }

    public String getName() {
        return name;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
        updateCompletionPercentage();
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
        updateCompletionPercentage();
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public void setProfileError(String error) {
        this.profileError = error;
    }

    public String getProfileError() {
        return profileError;
    }

    /**
     * Updates the profile completion percentage based on current field values.
     * This is called automatically when any profile field is updated.
     */
    private void updateCompletionPercentage() {
        int completedFields = 0;
        int totalFields = 5; // name, bio, fav_pokemon, profilePhotoPath, bannerPath

        if (name != null && !name.trim().isEmpty())
            completedFields++;
        if (bio != null && !bio.trim().isEmpty())
            completedFields++;
        if (fav_pokemon != null && !fav_pokemon.isEmpty() && !fav_pokemon.equals("None"))
            completedFields++;
        if (profilePhotoPath != null && !profilePhotoPath.isEmpty())
            completedFields++;
        if (bannerPath != null && !bannerPath.isEmpty())
            completedFields++;

        profileCompletionPercentage = (int) Math.round((completedFields * 100.0) / totalFields);
    }
}
