package use_case.user_profile;

/**
 * The output data for the User Profile use case.
 */
public class UserProfileOutputData {

    private final String username;
    private final String name;
    private final String bio;
    private final String favPokemon;
    private final String profilePhotoPath;
    private final String bannerPath;
    private final int profileCompletionPercentage;
    private final int bioCharacterCount;

    public UserProfileOutputData(String username, String name, String bio, String favPokemon, 
                                String profilePhotoPath, String bannerPath) {
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.favPokemon = favPokemon;
        this.profilePhotoPath = profilePhotoPath;
        this.bannerPath = bannerPath;
        this.bioCharacterCount = bio != null ? bio.length() : 0;
        this.profileCompletionPercentage = calculateCompletionPercentage(name, bio, favPokemon, profilePhotoPath, bannerPath);
    }

    public UserProfileOutputData(String username, String name, String bio, String favPokemon, 
                                String profilePhotoPath, String bannerPath, int profileCompletionPercentage, int bioCharacterCount) {
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.favPokemon = favPokemon;
        this.profilePhotoPath = profilePhotoPath;
        this.bannerPath = bannerPath;
        this.profileCompletionPercentage = profileCompletionPercentage;
        this.bioCharacterCount = bioCharacterCount;
    }

    /**
     * Calculates the profile completion percentage based on filled fields.
     * This is business logic that lives in the use case layer.
     * @param name display name
     * @param bio bio text
     * @param favPokemon favorite pokemon
     * @param profilePhotoPath profile photo path
     * @param bannerPath banner path
     * @return completion percentage (0-100)
     */
    private int calculateCompletionPercentage(String name, String bio, String favPokemon, 
                                              String profilePhotoPath, String bannerPath) {
        int completedFields = 0;
        int totalFields = 5; // name, bio, fav_pokemon, profilePhotoPath, bannerPath

        if (name != null && !name.trim().isEmpty())
            completedFields++;
        if (bio != null && !bio.trim().isEmpty())
            completedFields++;
        if (favPokemon != null && !favPokemon.isEmpty() && !favPokemon.equals("None"))
            completedFields++;
        if (profilePhotoPath != null && !profilePhotoPath.isEmpty())
            completedFields++;
        if (bannerPath != null && !bannerPath.isEmpty())
            completedFields++;

        return (int) Math.round((completedFields * 100.0) / totalFields);
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getFavPokemon() {
        return favPokemon;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public int getProfileCompletionPercentage() {
        return profileCompletionPercentage;
    }

    public int getBioCharacterCount() {
        return bioCharacterCount;
    }
}

