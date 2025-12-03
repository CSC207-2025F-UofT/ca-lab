package data_access;

import entity.Game;
import java.io.IOException;

public interface GameDataAccessInterface {

    /**
     * Returns true if a saved game exists on disk.
     */
    boolean hasSavedGame();

    /**
     * Load the saved game from storage.
     * @throws IOException if reading fails
     * @throws IllegalStateException if no saved game exists
     */
    Game loadGame() throws IOException;

    /**
     * Save the given game to storage.
     * @throws IOException if writing fails
     */
    void saveGame(Game game) throws IOException;

    /**
     * Delete any saved game from storage (e.g., after win/lose).
     * @throws IOException if deleting fails
     */
    void deleteGame() throws IOException;
}