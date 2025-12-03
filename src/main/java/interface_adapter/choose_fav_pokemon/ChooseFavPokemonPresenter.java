package interface_adapter.choose_fav_pokemon;

import interface_adapter.ViewManagerModel;
import use_case.choose_fav_pokemon.ChooseFavPokemonOutputBoundary;
import use_case.choose_fav_pokemon.ChooseFavPokemonOutputData;

/**
 * The presenter for the Choose Favourite Pokémon use case.
 */
public class ChooseFavPokemonPresenter implements ChooseFavPokemonOutputBoundary {
    private final ChooseFavPokemonViewModel chooseFavPokemonViewModel;
    private final ViewManagerModel viewManagerModel;
    //private final GameDashboard dashboard;

    public ChooseFavPokemonPresenter(ChooseFavPokemonViewModel chooseFavPokemonViewModel,
                                ViewManagerModel viewManagerModel) {
        this.chooseFavPokemonViewModel = chooseFavPokemonViewModel;
        this.viewManagerModel = viewManagerModel;
        //this.dashboard = dashboard;
    }

    /**
     * Sets up output data from the Choose Favourite Pokémon use case for display if no errors occur.
     *
     * @param outputData the output data
     */
    @Override
    public void prepareSuccessView(ChooseFavPokemonOutputData outputData) {
        chooseFavPokemonViewModel.getState().setFav_pokemon(outputData.getFavPokemon());
        chooseFavPokemonViewModel.getState().setBio(outputData.getBio());
        chooseFavPokemonViewModel.getState().setProfileError(null);
    }

    /**
     * Sets up output data from the Choose Favourite Pokémon use case for display if an error occurs.
     *
     * @param error the error message
     */
    @Override
    public void prepareFailView(String error) {
        chooseFavPokemonViewModel.getState().setProfileError(error);
    }
}