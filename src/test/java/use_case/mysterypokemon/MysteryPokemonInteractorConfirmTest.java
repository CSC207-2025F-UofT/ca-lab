package use_case.mysterypokemon;

import data_access.GameDataAccessInterface;
import data_access.PokemonDataAccessInterface;
import data_access.TypeMultiplierCalculator;
import entity.Game;
import entity.Pokemon;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MysteryPokemonInteractor.confirm(...) aiming for 100% coverage.
 * No Mockito: all fakes are hand-written.
 */
public class MysteryPokemonInteractorConfirmTest {

    // ===== Helpers =====

    /** Helper to construct a Pokemon. Adjust if your constructor has extra fields. */
    private Pokemon makePokemon(String name,
                                List<String> types,
                                boolean legendary,
                                boolean mythical,
                                int totalBaseStats,
                                String spriteUrl) {
        return new Pokemon(
                name,
                new ArrayList<>(types),
                legendary,
                mythical,
                totalBaseStats,
                spriteUrl
        );
    }

    private MysteryPokemonInteractor makeInteractor(
            PokemonDataAccessInterface pokemonDAO,
            GameDataAccessInterface gameDAO,
            TypeMultiplierCalculator calculator,
            TestPresenter presenter
    ) {
        return new MysteryPokemonInteractor(
                pokemonDAO,
                gameDAO,
                calculator,
                presenter
        );
    }

    // ===== 1. First guard: guessedName null / empty =====

    @Test
    void confirm_emptyGuess_showsPleaseMakeAGuess_andReturns() throws IOException {
        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        TestGameDAO gameDAO = new TestGameDAO();
        TestPresenter presenter = new TestPresenter();
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(1.0);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        MysteryPokemonInputData input = new MysteryPokemonInputData("");

        interactor.confirm(input);

        assertEquals("Please make a guess.", presenter.lastError);
        assertNull(presenter.lastSuccess);

        assertFalse(gameDAO.loadCalled);
        assertFalse(gameDAO.saveCalled);
        assertFalse(gameDAO.deleteCalled);
    }

    @Test
    void confirm_nullGuess_showsPleaseMakeAGuess_andReturns() throws IOException {
        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        TestGameDAO gameDAO = new TestGameDAO();
        TestPresenter presenter = new TestPresenter();
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(1.0);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        MysteryPokemonInputData input = new MysteryPokemonInputData(null);

        interactor.confirm(input);

        assertEquals("Please make a guess.", presenter.lastError);
        assertNull(presenter.lastSuccess);
        assertFalse(gameDAO.loadCalled);
    }

    // ===== 2. loadGame() try/catch cases =====

    @Test
    void confirm_noActiveGame_illegalStateFromLoad_showsStartFirstError() throws IOException {
        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        TestGameDAO gameDAO = new TestGameDAO();
        gameDAO.loadIllegalStateException = new IllegalStateException("no game");

        TestPresenter presenter = new TestPresenter();
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(1.0);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        MysteryPokemonInputData input = new MysteryPokemonInputData("Pikachu");

        interactor.confirm(input);

        assertEquals("No active game. Please press Start first.", presenter.lastError);
        assertNull(presenter.lastSuccess);

        assertTrue(gameDAO.loadCalled);
        assertFalse(gameDAO.saveCalled);
        assertFalse(gameDAO.deleteCalled);
    }

    @Test
    void confirm_loadGameIOException_showsLoadError() throws IOException {
        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        TestGameDAO gameDAO = new TestGameDAO();
        gameDAO.loadIOException = new IOException("disk read failed");

        TestPresenter presenter = new TestPresenter();
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(1.0);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        MysteryPokemonInputData input = new MysteryPokemonInputData("Pikachu");

        interactor.confirm(input);

        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.startsWith("Failed to load current game:"));
        assertNull(presenter.lastSuccess);

        assertTrue(gameDAO.loadCalled);
        assertFalse(gameDAO.saveCalled);
        assertFalse(gameDAO.deleteCalled);
    }

    // ===== 3. PokemonNotFoundException path =====

    @Test
    void confirm_pokemonNotFound_showsUnavailableError() throws IOException {
        // Game exists
        TestGameDAO gameDAO = new TestGameDAO();
        gameDAO.game = new Game("Pikachu", 10, 0);

        // Pokemon DAO will throw for "Missingmon"
        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        pokemonDAO.throwNotFoundFor("Missingmon");

        TestPresenter presenter = new TestPresenter();
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(1.0);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        MysteryPokemonInputData input = new MysteryPokemonInputData("Missingmon");

        interactor.confirm(input);

        assertEquals("That Pokémon is not available.", presenter.lastError);
        assertNull(presenter.lastSuccess);

        assertTrue(gameDAO.loadCalled);
        assertFalse(gameDAO.saveCalled);
        assertFalse(gameDAO.deleteCalled);
    }

    // ===== 4. Correct guess path (gameOver via correct == true) =====

    @Test
    void confirm_correctGuess_deletesGame_andOutputsGameOverTrue() throws IOException {
        TestGameDAO gameDAO = new TestGameDAO();
        gameDAO.game = new Game("Pikachu", 10, 0);

        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        Pokemon pikachu = makePokemon(
                "Pikachu",
                List.of("electric"),
                false,
                false,
                320,
                "no sprite"
        );
        pokemonDAO.addPokemon(pikachu);  // both guess and answer will be this Pokémon

        TestPresenter presenter = new TestPresenter();
        // multiplier 1.0 → mult1 == true, others false
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(1.0);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        MysteryPokemonInputData input = new MysteryPokemonInputData("Pikachu");

        interactor.confirm(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        // Because correct == true, we expect gameOver == true
        // and deleteGame() called instead of saveGame().
        assertTrue(gameDAO.loadCalled);
        assertFalse(gameDAO.saveCalled);
        assertTrue(gameDAO.deleteCalled);
        assertNull(gameDAO.game); // deleted

        MysteryPokemonOutputData out = presenter.lastSuccess;
        assertTrue(out.isCorrect());
        assertTrue(out.isGameOver());
        assertEquals(10, out.getGuessesLeft());

        // same main type (electric vs electric)
        assertTrue(out.isSameMainType());
        // only mult1 should be true for multiplier 1.0
        assertFalse(out.isMult0());
        assertFalse(out.isMult025());
        assertFalse(out.isMult05());
        assertTrue(out.isMult1());
        assertFalse(out.isMult2());
        assertFalse(out.isMult4());
    }

    // ===== 5. Wrong guess, not game over → saveGame =====

    @Test
    void confirm_wrongGuess_notGameOver_savesGame() throws IOException {
        TestGameDAO gameDAO = new TestGameDAO();
        gameDAO.game = new Game("Pikachu", 10, 0);

        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        Pokemon pikachu = makePokemon(
                "Pikachu",
                List.of("electric"),
                false,
                false,
                320,
                "no sprite"
        );
        Pokemon bulbasaur = makePokemon(
                "Bulbasaur",
                List.of("grass", "poison"),
                false,
                false,
                318,
                "no sprite"
        );
        pokemonDAO.addPokemon(pikachu);
        pokemonDAO.addPokemon(bulbasaur);

        TestPresenter presenter = new TestPresenter();
        // multiplier 2.0 → mult2 true
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(2.0);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        MysteryPokemonInputData input = new MysteryPokemonInputData("Bulbasaur");

        interactor.confirm(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        // Should have consumed one guess
        assertNotNull(gameDAO.game);
        assertEquals(1, gameDAO.game.getCurrGuess());
        assertEquals(9, gameDAO.game.getGuessesLeft());

        // Not game over yet → saveGame called, delete not called
        assertTrue(gameDAO.saveCalled);
        assertFalse(gameDAO.deleteCalled);

        MysteryPokemonOutputData out = presenter.lastSuccess;
        assertFalse(out.isCorrect());
        assertFalse(out.isGameOver());
        assertEquals(9, out.getGuessesLeft());

        // Different main type: GRASS vs ELECTRIC
        assertFalse(out.isSameMainType());
        // Now multiplier 2.0 → mult2 true
        assertFalse(out.isMult0());
        assertFalse(out.isMult025());
        assertFalse(out.isMult05());
        assertFalse(out.isMult1());
        assertTrue(out.isMult2());
        assertFalse(out.isMult4());
    }

    // ===== 6. Wrong guess, game over via isOver() → deleteGame =====

    @Test
    void confirm_wrongGuess_lastLife_gameOver_deletesGame() throws IOException {
        // Game with only 1 guess allowed.
        TestGameDAO gameDAO = new TestGameDAO();
        gameDAO.game = new Game("Pikachu", 1, 0);

        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        Pokemon pikachu = makePokemon(
                "Pikachu",
                List.of("electric"),
                false,
                false,
                320,
                "no sprite"
        );
        Pokemon bulbasaur = makePokemon(
                "Bulbasaur",
                List.of("grass", "poison"),
                false,
                false,
                318,
                "no sprite"
        );
        pokemonDAO.addPokemon(pikachu);
        pokemonDAO.addPokemon(bulbasaur);

        TestPresenter presenter = new TestPresenter();
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(0.5);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        MysteryPokemonInputData input = new MysteryPokemonInputData("Bulbasaur");

        interactor.confirm(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        // After one wrong guess, isOver() should be true → gameOver true → deleteGame
        assertTrue(gameDAO.deleteCalled);
        assertFalse(gameDAO.saveCalled);
        assertNull(gameDAO.game);

        MysteryPokemonOutputData out = presenter.lastSuccess;
        assertFalse(out.isCorrect());
        assertTrue(out.isGameOver());
        assertEquals(0, out.getGuessesLeft());
    }

    // ===== 7. IOException when saving game (gameOver == false) =====

    @Test
    void confirm_saveGameIOException_showsSaveError_andNoSuccess() throws IOException {
        TestGameDAO gameDAO = new TestGameDAO();
        gameDAO.game = new Game("Pikachu", 10, 0);
        gameDAO.saveIOException = new IOException("disk full");

        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        Pokemon pikachu = makePokemon(
                "Pikachu",
                List.of("electric"),
                false,
                false,
                320,
                "no sprite"
        );
        Pokemon bulbasaur = makePokemon(
                "Bulbasaur",
                List.of("grass", "poison"),
                false,
                false,
                318,
                "no sprite"
        );
        pokemonDAO.addPokemon(pikachu);
        pokemonDAO.addPokemon(bulbasaur);

        TestPresenter presenter = new TestPresenter();
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(1.0);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        MysteryPokemonInputData input = new MysteryPokemonInputData("Bulbasaur");

        interactor.confirm(input);

        // We should hit the catch (IOException e) around saveGame
        assertNull(presenter.lastSuccess);
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.startsWith("Failed to save game:"));
    }

    // ===== 8. IOException when deleting game (gameOver == true) =====

    @Test
    void confirm_deleteGameIOException_showsSaveError_andNoSuccess() throws IOException {
        TestGameDAO gameDAO = new TestGameDAO();
        gameDAO.game = new Game("Pikachu", 1, 0);  // one life

        // deleteGame will throw
        gameDAO.deleteIOException = new IOException("delete failed");

        TestPokemonDAO pokemonDAO = new TestPokemonDAO();
        Pokemon pikachu = makePokemon(
                "Pikachu",
                List.of("electric"),
                false,
                false,
                320,
                "no sprite"
        );
        Pokemon bulbasaur = makePokemon(
                "Bulbasaur",
                List.of("grass", "poison"),
                false,
                false,
                318,
                "no sprite"
        );
        pokemonDAO.addPokemon(pikachu);
        pokemonDAO.addPokemon(bulbasaur);

        TestPresenter presenter = new TestPresenter();
        TypeMultiplierCalculator calculator = new FixedMultiplierCalculator(1.0);

        MysteryPokemonInteractor interactor =
                makeInteractor(pokemonDAO, gameDAO, calculator, presenter);

        // Wrong guess → gameOver via isOver() → deleteGame throws IOException
        MysteryPokemonInputData input = new MysteryPokemonInputData("Bulbasaur");

        interactor.confirm(input);

        assertNull(presenter.lastSuccess);
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.startsWith("Failed to save game:"));
    }

    // ===== Test doubles =====

    /**
     * Configurable in-memory Game DAO.
     */
    private static class TestGameDAO implements GameDataAccessInterface {
        Game game;

        boolean loadCalled = false;
        boolean saveCalled = false;
        boolean deleteCalled = false;

        IOException loadIOException = null;
        IllegalStateException loadIllegalStateException = null;
        IOException saveIOException = null;
        IOException deleteIOException = null;

        @Override
        public boolean hasSavedGame() {
            return game != null;
        }

        @Override
        public Game loadGame() throws IOException {
            loadCalled = true;
            if (loadIllegalStateException != null) {
                throw loadIllegalStateException;
            }
            if (loadIOException != null) {
                throw loadIOException;
            }
            if (game == null) {
                throw new IllegalStateException("No game");
            }
            return game;
        }

        @Override
        public void saveGame(Game game) throws IOException {
            saveCalled = true;
            if (saveIOException != null) {
                throw saveIOException;
            }
            this.game = game;
        }

        @Override
        public void deleteGame() throws IOException {
            deleteCalled = true;
            if (deleteIOException != null) {
                throw deleteIOException;
            }
            this.game = null;
        }
    }

    /**
     * In-memory Pokemon DAO that can throw PokemonNotFoundException for specific names.
     */
    private static class TestPokemonDAO implements PokemonDataAccessInterface {
        private final Map<String, Pokemon> map = new HashMap<>();
        private final Set<String> missing = new HashSet<>();

        void addPokemon(Pokemon pokemon) {
            map.put(pokemon.getName().toLowerCase(), pokemon);
        }

        void throwNotFoundFor(String name) {
            missing.add(name.toLowerCase());
        }

        @Override
        public Pokemon getByName(String name) throws PokemonNotFoundException {
            if (name == null) {
                throw new PokemonNotFoundException("null name");
            }
            String key = name.toLowerCase();
            if (missing.contains(key)) {
                throw new PokemonNotFoundException("not found: " + name);
            }
            Pokemon p = map.get(key);
            if (p == null) {
                throw new PokemonNotFoundException("not found: " + name);
            }
            return p;
        }

        // Add stubs for other methods if your interface has more.
    }

    /**
     * Fixed-multiplier calculator so we don't depend on TypeFetcher.
     */
    private static class FixedMultiplierCalculator extends TypeMultiplierCalculator {
        private final double fixed;

        public FixedMultiplierCalculator(double fixed) {
            super(null); // we don't use TypeFetcher in tests
            this.fixed = fixed;
        }

        @Override
        public double calcMult(Pokemon guess, Pokemon answer) {
            return fixed;
        }
    }

    /**
     * Presenter that just records the last success or error.
     */
    private static class TestPresenter implements MysteryPokemonOutputBoundary {
        MysteryPokemonOutputData lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(MysteryPokemonOutputData outputData) {
            this.lastSuccess = outputData;
            this.lastError = null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
            this.lastSuccess = null;
        }
    }
}
