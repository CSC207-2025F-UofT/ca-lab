package use_case.mysterypokemon;

public class MysteryPokemonInputData {
    private final String guessedName;

    public MysteryPokemonInputData(String guessedName) {
        this.guessedName = guessedName;
    }

    public String getGuessedName() {
        return guessedName;
    }
}
