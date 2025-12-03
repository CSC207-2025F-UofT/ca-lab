package interface_adapter.akinator;

import use_case.akinator.AkinatorInputBoundary;

/**
 * The controller for the Akinator (classic mode) use case.
 */
public class AkinatorController {

    private final AkinatorInputBoundary interactor;

    public AkinatorController(AkinatorInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Starts the Akinator game.
     */
    public void start() {
        interactor.start();
    }

    /**
     * Resets the Akinator game.
     */
    public void reset() {
        interactor.reset();
    }

    /**
     * Instructs the interactor when user answers yes to a question.
     */
    public void answerYes() {
        interactor.answerYes();
    }

    /**
     * Instructs the interactor when user answers no to a question.
     */
    public void answerNo() {
        interactor.answerNo();
    }

    /**
     * Instructs the interactor when user answers "don't know" to a question.
     */
    public void answerUnknown() {
        interactor.answerUnknown();
    }

    /**
     * Instructs the interactor when user confirms whether the guess is correct or incorrect.
     */
    public void confirmGuess(boolean correct) {
        interactor.confirmGuess(correct);
    }

    /**
     * Allows the user to input the actual Pokémon they were thinking of.
     */
    public void revealPokemon(String pokemonName) {
        interactor.revealPokemon(pokemonName);
    }
}
