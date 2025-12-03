package data_access;

import entity.Game;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileGameDataAccess implements GameDataAccessInterface {

    private final Path filePath;

    /**
     * @param filePath path to JSON file, e.g. "game_state.json"
     */
    public FileGameDataAccess(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    @Override
    public boolean hasSavedGame() {
        return Files.exists(filePath);
    }

    @Override
    public Game loadGame() throws IOException {
        if (!hasSavedGame()) {
            throw new IllegalStateException("No saved game at " + filePath);
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader =
                     Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        try {
            JSONObject json = new JSONObject(sb.toString());
            String target = json.getString("targetPokemon");
            int currGuess = json.getInt("currGuess");
            int maxGuesses = json.getInt("maxGuesses");

            return new Game(target, maxGuesses, currGuess);
        } catch (JSONException e) {
            throw new IOException("Failed to parse saved game JSON", e);
        }
    }

    @Override
    public void saveGame(Game game) throws IOException {
        JSONObject json = new JSONObject();
        json.put("targetPokemon", game.getTarget());
        json.put("currGuess", game.getCurrGuess());
        json.put("maxGuesses", game.getMaxGuesses());

        try (BufferedWriter writer =
                     Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            // pretty-print with indentation 2
            writer.write(json.toString(2));
        }
    }

    @Override
    public void deleteGame() throws IOException {
        Files.deleteIfExists(filePath);
    }
}