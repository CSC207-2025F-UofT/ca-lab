package interface_adapter.mysterypokemon;

import interface_adapter.ViewModel;

public class MysteryPokemonViewModel extends ViewModel<MysteryPokemonState>{

    public MysteryPokemonViewModel(){
        super("mysterypokemon");
        setState(new MysteryPokemonState());
    }

}
