package data_access;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.SpotifyUser;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class SpotifyUserDataAccessObject {

    private static SpotifyUserDataAccessObject instance;
    private final Map<String, SpotifyUser> userIdToUser = new HashMap<>();
    private final String FILE_PATH = "spotify_users.json";
    private final Gson gson = new Gson();

    private SpotifyUserDataAccessObject() {
        loadFromJson();
    }

    public static SpotifyUserDataAccessObject getInstance() {
        if (instance == null) instance = new SpotifyUserDataAccessObject();
        return instance;
    }

    /** Load existing users on startup */
    private void loadFromJson() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<SpotifyUser>>(){}.getType();
            List<SpotifyUser> users = gson.fromJson(reader, listType);

            if (users != null) {
                for (SpotifyUser u : users) {
                    userIdToUser.put(u.getSpotifyUserId(), u);
                }
            }
            System.out.println("Loaded " + userIdToUser.size() + " Spotify users from disk.");

        } catch (Exception e) {
            System.out.println("No existing Spotify user file found. Starting fresh.");
        }
    }

    /** Save all users to JSON */
    private void saveToJson() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(gson.toJson(userIdToUser.values()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Add or update a Spotify user */
    public void addUser(SpotifyUser user) {
        if (user != null) {
            userIdToUser.put(user.getSpotifyUserId(), user);
            saveToJson(); // persist immediately
        }
    }

    public SpotifyUser getUserById(String id) {
        return userIdToUser.get(id);
    }

    public List<SpotifyUser> getAllUsers() {
        return new ArrayList<>(userIdToUser.values());
    }
}
