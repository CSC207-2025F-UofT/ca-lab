package entity;

/**
 * Factory for creating User objects.
 */
public class UserFactory {

    /**
     * Creates a new user with no info other than username and password.
     *
     * @param name the username of the new user
     * @param password the password of the new user
     */
    public User create(String name, String password) {
        return new User(name, password);
    }

    /**
     * Creates a new user given username, password, score, bio, and a favourite Pokémon.
     *
     * @param name the username of the new user
     * @param password the password of the new user
     * @param score the score of the user from the Pokenator game
     * @param bio the user's bio
     * @param fav_pokemon the user's favourite Pokémon
     */
    public User create(String name, String password, int score, String bio, String fav_pokemon) {
        return new User(name, password, score, bio, fav_pokemon);
    }

    /**
     * Creates a new user with a profile photo and banner given username, password, score,
     * bio, and a favourite Pokémon.
     *
     * @param name the username of the new user
     * @param password the password of the new user
     * @param score the score of the user from the Pokenator game
     * @param bio the user's bio
     * @param fav_pokemon the user's favourite Pokémon
     * @param profilePhotoPath the path to the user's profile picture
     * @param bannerPath the path to the picture on the user's banner
     */
    public User create(String name, String password, int score, String bio, String fav_pokemon, String profilePhotoPath, String bannerPath) {
        return new User(name, password, score, bio, fav_pokemon, profilePhotoPath, bannerPath);
    }
}
