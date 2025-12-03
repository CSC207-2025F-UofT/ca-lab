package use_case.mysterypokemon;

import entity.Game;
import entity.Pokemon;
import data_access.Gen1Loader;
import data_access.TypeMultiplierCalculator;
import data_access.GameDataAccessInterface;
import data_access.PokemonDataAccessInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MysteryPokemonInteractor implements MysteryPokemonInputBoundary {

    private final PokemonDataAccessInterface pokemonDAO;
    private final GameDataAccessInterface gameDAO;
    private final TypeMultiplierCalculator calculator;
    private final MysteryPokemonOutputBoundary presenter;

    private final Gen1Loader loader;
    private final String gen1PokemonPath;

    private static final int MAX_GUESSES = 10;

    public MysteryPokemonInteractor(PokemonDataAccessInterface pokemonDAO,
                                    GameDataAccessInterface gameDAO,
                                    TypeMultiplierCalculator calculator,
                                    MysteryPokemonOutputBoundary presenter) {
        this(pokemonDAO, gameDAO, calculator, presenter,
                new Gen1Loader(),
                "src/main/resources/gen1Pokemon.json");
    }

    // SECOND constructor (package-private or public) for tests
    MysteryPokemonInteractor(PokemonDataAccessInterface pokemonDAO,
                             GameDataAccessInterface gameDAO,
                             TypeMultiplierCalculator calculator,
                             MysteryPokemonOutputBoundary presenter,
                             Gen1Loader loader,
                             String gen1PokemonPath) {
        this.pokemonDAO = pokemonDAO;
        this.gameDAO = gameDAO;
        this.calculator = calculator;
        this.presenter = presenter;
        this.loader = loader;
        this.gen1PokemonPath = gen1PokemonPath;
    }

    @Override
    public void confirm(MysteryPokemonInputData inputData) throws IOException {
        String guessedName = inputData.getGuessedName();
        if (guessedName == null || guessedName.isEmpty()) {
            presenter.prepareFailView("Please make a guess.");
            return;
        }

        // Load current game from JSON
        final Game game;
        try {
            game = gameDAO.loadGame();
        } catch (IllegalStateException e) {
            presenter.prepareFailView("No active game. Please press Start first.");
            return;
        } catch (IOException e) {
            presenter.prepareFailView("Failed to load current game: " + e.getMessage());
            return;
        }

        String targetName = game.getTarget();

        Pokemon guess;
        Pokemon answer;
        try {
            guess = pokemonDAO.getByName(guessedName);
            answer = pokemonDAO.getByName(targetName);
        } catch (PokemonNotFoundException e) {
            presenter.prepareFailView("That Pokémon is not available.");
            return;
        }

        // Types / same main type
        List<String> guessTypes = guess.getTypes();
        List<String> answerTypes = answer.getTypes();
        boolean sameMainType = !guessTypes.isEmpty()
                && !answerTypes.isEmpty()
                && guessTypes.get(0).equalsIgnoreCase(answerTypes.get(0));

        // Damage multiplier
        double multiplier = calculator.calcMult(guess, answer);

        boolean mult0   = multiplier == 0.0;
        boolean mult025 = multiplier == 0.25;
        boolean mult05  = multiplier == 0.5;
        boolean mult1   = multiplier == 1.0;
        boolean mult2   = multiplier == 2.0;
        boolean mult4   = multiplier == 4.0;

        // Legendary / mythical status
        boolean sameLegendaryStatus =
                guess.getLegendary() == answer.getLegendary();

        boolean sameMythicalStatus =
                guess.getMythical() == answer.getMythical();

        // Total base stats comparison
        int guessTbs = guess.getTotalBaseStats();
        int answerTbs = answer.getTotalBaseStats();
        boolean tbsLess = guessTbs < answerTbs;
        boolean tbsSame = guessTbs == answerTbs;
        boolean tbsMore = guessTbs > answerTbs;

        // Correct guess?
        boolean correct = guess.getName().equalsIgnoreCase(answer.getName());

        // Update game state
        if (!correct) {
            game.makeGuess();
        }

        int guessesLeft = game.getGuessesLeft();
        boolean gameOver = correct || game.isOver();

        // Persist / delete game depending on state
        try {
            if (gameOver) {
                gameDAO.deleteGame();
            } else {
                gameDAO.saveGame(game);
            }
        } catch (IOException e) {
            presenter.prepareFailView("Failed to save game: " + e.getMessage());
            return;
        }

        // Use the *answer* Pokémon for reveal info
        String answerName = answer.getName();
        String answerSpriteUrl = answer.getSpriteUrl();

        MysteryPokemonOutputData outputData = new MysteryPokemonOutputData(
                guessesLeft,
                sameMainType,
                mult0,
                mult025,
                mult05,
                mult1,
                mult2,
                mult4,
                sameLegendaryStatus,
                sameMythicalStatus,
                tbsLess,
                tbsSame,
                tbsMore,
                gameOver,
                correct,
                answerName,
                answerSpriteUrl
        );

        presenter.prepareSuccessView(outputData);
    }

    /**
     * Starts a new game: picks a random Gen 1 Pokémon, saves the Game,
     * and sends initial state (all hints false, full guesses left) to the presenter.
     */
    public void start() {
        try {
            ArrayList<String> names = loader.loadPokemonNames(gen1PokemonPath);

            if (names.isEmpty()) {
                presenter.prepareFailView("No Pokémon found to start game!");
                return;
            }

            Random random = new Random();
            String targetName = names.get(random.nextInt(names.size()));

            Game newGame = new Game(targetName, MAX_GUESSES, 0);

            // Save new game
            gameDAO.saveGame(newGame);

            // Initial output: max guesses, all hints false, no answer revealed
            MysteryPokemonOutputData outputData = new MysteryPokemonOutputData(
                    MAX_GUESSES,
                    false,  // sameMainType
                    false,  // mult0
                    false,  // mult025
                    false,  // mult05
                    false,  // mult1
                    false,  // mult2
                    false,  // mult4
                    false,  // sameLegendaryStatus
                    false,  // sameMythicalStatus
                    false,  // tbsLess
                    false,  // tbsSame
                    false,  // tbsMore
                    false,  // gameOver
                    false,  // correct
                    "",     // answerName
                    ""      // answerSpriteUrl
            );

            presenter.prepareSuccessView(outputData);

        } catch (IOException e) {
            presenter.prepareFailView("Failed to save new game: " + e.getMessage());
        } catch (RuntimeException e) {
            presenter.prepareFailView("Failed to start game: " + e.getMessage());
        }
    }

    public void reset() {
        start();
    }
}
