package interface_adapter.choose_fav_pokemon;

import entity.User;
import interface_adapter.ViewModel;

/**
 * The view model for the Choose Favourite Pokémon use case.
 */
public class ChooseFavPokemonViewModel extends ViewModel<ChooseFavPokemonState> {

    public ChooseFavPokemonViewModel(User u) {
        super("Edit Profile");
        setState(new ChooseFavPokemonState(u));
    }
}
