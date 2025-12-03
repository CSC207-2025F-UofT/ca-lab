package use_case.choose_fav_pokemon;

/**
 * The input data for the Choose Favourite Pokemon Use Case.
 */
public class ChooseFavPokemonInputData {
    private final String username;
    private final String password;
    private final int score;
    private final String bio;
    private final String fav_pokemon;

    public ChooseFavPokemonInputData(String username, String password, int score, String bio, String fav_pokemon) {
        this.username = username;
        this.password = password;
        this.score = score;
        this.bio = bio;
        this.fav_pokemon = fav_pokemon;
    }

    String getUsername() { return username; }
    String getPassword() { return password; }
    int getScore() { return score; }
    String getBio() { return bio; }
    String getFav_pokemon() { return fav_pokemon; }
}
