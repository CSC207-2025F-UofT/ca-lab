package use_case.mysterypokemon;

public interface MysteryPokemonOutputBoundary {

    void prepareSuccessView(MysteryPokemonOutputData outputData);

    void prepareFailView(String errorMessage);
}

