package use_case.choose_fav_pokemon;

/**
 * The Choose Favourite Pokemon Use Case.
 */
public interface ChooseFavPokemonInputBoundary {

    /**
     * Execute the Choose Favourite Pokemon Use Case.
     * @param chooseFavPokemonInputData the input data for this use case
     */
    void execute(ChooseFavPokemonInputData chooseFavPokemonInputData);
}
