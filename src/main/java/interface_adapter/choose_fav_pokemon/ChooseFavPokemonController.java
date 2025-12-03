package interface_adapter.choose_fav_pokemon;

import use_case.choose_fav_pokemon.ChooseFavPokemonInputBoundary;
import use_case.choose_fav_pokemon.ChooseFavPokemonInputData;

/**
 * The controller for the Choose Favourite Pokémon use case.
 */
public class ChooseFavPokemonController {
    private final ChooseFavPokemonInputBoundary chooseFavPokemonInteractor;

    public ChooseFavPokemonController(ChooseFavPokemonInputBoundary chooseFavPokemonInteractor) {
        this.chooseFavPokemonInteractor = chooseFavPokemonInteractor;
    }

    /**
     * Executes the Choose Favourite Pokemon Use Case.
     * @param username username whose profile will change
     * @param password user's password
     * @param score user's score
     * @param bio the new bio
     * @param fav_pokemon the new favourite pokemon
     */
    public void execute(String username, String password, int score, String bio, String fav_pokemon) {
        final ChooseFavPokemonInputData chooseFavPokemonInputData = new ChooseFavPokemonInputData(username, password,
                score, bio, fav_pokemon);

        chooseFavPokemonInteractor.execute(chooseFavPokemonInputData);
    }
}
