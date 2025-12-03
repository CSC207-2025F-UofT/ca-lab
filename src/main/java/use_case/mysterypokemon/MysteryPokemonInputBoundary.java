package use_case.mysterypokemon;

import java.io.IOException;

public interface MysteryPokemonInputBoundary {

    void confirm(MysteryPokemonInputData inputData) throws IOException;

    void start();

    void reset();
}

