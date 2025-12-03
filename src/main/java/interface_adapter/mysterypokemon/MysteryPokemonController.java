package interface_adapter.mysterypokemon;

import use_case.mysterypokemon.MysteryPokemonInputBoundary;
import use_case.mysterypokemon.MysteryPokemonInputData;

import java.io.IOException;

public class MysteryPokemonController {

    private final MysteryPokemonInputBoundary interactor;

    public MysteryPokemonController(MysteryPokemonInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void confirm(String guessedName) throws IOException {
        MysteryPokemonInputData inputData = new MysteryPokemonInputData(guessedName);
        interactor.confirm(inputData);
    }

    public void start() {
        interactor.start();
    }

    public void reset() {
        interactor.reset();
    }
}


