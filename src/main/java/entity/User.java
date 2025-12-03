package entity;

/**
 * An entity representing a user's account.
 */
public class User {

    private String name;
    private String password;

    private int score;
    private String bio;
    private String fav_pokemon; // NAME of the pokemon
    private String profilePhotoPath; // Path to profile photo
    private String bannerPath; // Path to banner image

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * 
     * @param name     the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String name, String password) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        this.name = name;
        this.password = password;
        this.score = 0;
        this.bio = "";
        this.fav_pokemon = null;
        this.profilePhotoPath = null;
        this.bannerPath = null;
    }

    public User(String name, String password, int score, String bio, String fav_pokemon) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        this.name = name;
        this.password = password;
        this.score = score;
        this.bio = bio;
        this.fav_pokemon = fav_pokemon;
        this.profilePhotoPath = null;
        this.bannerPath = null;
    }

    public User(String name, String password, int score, String bio, String fav_pokemon, String profilePhotoPath,
            String bannerPath) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        this.name = name;
        this.password = password;
        this.score = score;
        this.bio = bio;
        this.fav_pokemon = fav_pokemon;
        this.profilePhotoPath = profilePhotoPath;
        this.bannerPath = bannerPath;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public String getBio() {
        return bio;
    }

    public String getFavPokemon() {
        return fav_pokemon;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
    }

    public void setName(String name) {
        this.name = name;
    }

}
