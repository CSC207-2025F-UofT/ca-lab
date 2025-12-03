package entity;

public class Game {
    private final String targetPokemon;
    private int currGuess;
    private final int maxGuesses;

    public Game(String targetPokemon, int maxGuesses, int currGuess) {
        this.targetPokemon = targetPokemon;
        this.maxGuesses = maxGuesses;
        this.currGuess = currGuess;
    }

    public String getTarget() { return targetPokemon; }

    public int getCurrGuess() { return currGuess; }

    public void makeGuess() { currGuess++; }

    public int getMaxGuesses() { return maxGuesses; }

    public int getGuessesLeft() { return maxGuesses - currGuess; }

    public boolean isOver() {
        return currGuess >= maxGuesses;
    }
}