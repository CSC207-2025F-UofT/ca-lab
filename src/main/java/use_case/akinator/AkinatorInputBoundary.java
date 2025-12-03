package use_case.akinator;

/**
 * The input boundary for the Akinator use case.
 */
public interface AkinatorInputBoundary {
    void start();
    void reset();
    void answerYes();
    void answerNo();
    void answerUnknown();
    void confirmGuess(boolean correct);
    void revealPokemon(String pokemonName);
}
