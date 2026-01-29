package data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import use_case.loyalty_score.LoyaltyScoreDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of LoyaltyScoreDataAccessInterface
 * The file is formatted as: loyaltyscore_userid
 * {
 *   "loyalty_scores": [
 *     {
 *       "date": "2023-11-23",
 *       "artist_name": "Artist1",
 *       "score": 85
 * }
 */

public class LoyaltyScoreDataAccessObject implements LoyaltyScoreDataAccessInterface {


    @Override
    public Map<String, Integer> getLoyaltyDate(String userid, String date) {
        Map<String, Integer> loyaltyScores = new HashMap<>();
        JSONObject rootNode = readUserFile(userid);
        if (rootNode == null) {
            return loyaltyScores;  // Early return if the file doesn't exist
        }

        JSONArray loyaltyScoresArray = rootNode.optJSONArray("loyalty_scores");
        if (loyaltyScoresArray == null) {
            return loyaltyScores;  // Early return if the loyalty_scores array doesn't exist
        }

        loyaltyScoresArray.forEach(entry -> {
            JSONObject loyaltyScore = (JSONObject) entry;
            if (loyaltyScore.getString("date").equals(date)) {
                String artistName = loyaltyScore.getString("artist_name");
                int score = loyaltyScore.getInt("score");
                loyaltyScores.put(artistName, score);
            }
        });
        return loyaltyScores;
    }

    @Override
    public boolean loyaltyScoreExists(String userid, String date, String artist_name) {
        JSONObject rootNode = readUserFile(userid);
        if (rootNode == null) {
            return false;  // Early return if the file doesn't exist
        }

        JSONArray loyaltyScoresArray = rootNode.optJSONArray("loyalty_scores");
        if (loyaltyScoresArray == null) {
            return false;  // Early return if the loyalty_scores array doesn't exist
        }

        for (Object entry : loyaltyScoresArray) {
            JSONObject loyaltyScore = (JSONObject) entry;
            if (loyaltyScore.getString("date").equals(date) &&
                    loyaltyScore.getString("artist_name").equals(artist_name)) {
                return true;  // Return true as soon as we find a match
            }
        }
        return false;  // No match found
    }

    @Override
    public void saveLoyalty(String userid, String date, String artist_name, int score) {
        JSONObject rootNode = readUserFile(userid);

        // Ensure rootNode is not null
        if (rootNode == null) {
            rootNode = new JSONObject();  // Initialize rootNode if it's null
        }

        // Ensure loyalty_scores array exists
        JSONArray loyaltyScoresArray = rootNode.optJSONArray("loyalty_scores");
        if (loyaltyScoresArray == null) {
            loyaltyScoresArray = new JSONArray();  // Create a new empty array if it's null
            rootNode.put("loyalty_scores", loyaltyScoresArray);  // Add the array to rootNode
        }

        // Create new loyalty score entry
        JSONObject newLoyaltyScore = new JSONObject();
        newLoyaltyScore.put("date", date);
        newLoyaltyScore.put("artist_name", artist_name);
        newLoyaltyScore.put("score", score);

        // Add the new loyalty score to the array
        loyaltyScoresArray.put(newLoyaltyScore);

        // Write the updated data back to the file
        writeUserFile(userid, rootNode, loyaltyScoresArray);
    }

    @Override
    public Map<String, Integer> getLoyaltyArtist(String userid, String artist_name) {
        Map<String, Integer> loyaltyScores = new HashMap<>();
        JSONObject rootNode = readUserFile(userid);
        if (rootNode == null) {
            return loyaltyScores;  // Early return if the file doesn't exist
        }

        JSONArray loyaltyScoresArray = rootNode.optJSONArray("loyalty_scores");
        if (loyaltyScoresArray == null) {
            return loyaltyScores;  // Early return if the loyalty_scores array doesn't exist
        }

        loyaltyScoresArray.forEach(entry -> {
            JSONObject loyaltyScore = (JSONObject) entry;
            if (loyaltyScore.getString("artist_name").equals(artist_name)) {
                String date = loyaltyScore.getString("date");
                int score = loyaltyScore.getInt("score");
                loyaltyScores.put(date, score);
            }
        });
        return loyaltyScores;
    }

    // Helper method to get the file for a specific user
    private File getUserFile(String userid) {
        return new File("loyaltyscore_" + userid + ".json");
    }

    // Helper method to read the user's file and return a JSONObject
    private JSONObject readUserFile(String userid) {
        File userFile = getUserFile(userid);
        if (!userFile.exists()) {
            return null;  // Return null if the file doesn't exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }

            // If the file is empty, return a new JSONObject with an empty "loyalty_scores" array
            if (jsonContent.length() == 0) {
                JSONObject emptyRoot = new JSONObject();
                emptyRoot.put("loyalty_scores", new JSONArray());
                return emptyRoot;
            }

            return new JSONObject(jsonContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Return null in case of an error reading the file
        }
    }

    // Helper method to write the JSONObject back to the user's file
    private void writeUserFile(String userid, JSONObject rootNode, JSONArray loyaltyScoresArray) {
        rootNode.put("loyalty_scores", loyaltyScoresArray);
        try (FileWriter fileWriter = new FileWriter(getUserFile(userid))) {
            fileWriter.write(rootNode.toString(4)); // Pretty print with indentation level 4
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
