package data_access;

import entity.Group;
import entity.SpotifyUser;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.GroupDataAccessInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDataAccessObject implements GroupDataAccessInterface {

    private final File groupFile = new File("groups.json");

    @Override
    public boolean existsByName(String groupName) {
        JSONArray groups = readGroupsFile();
        for (Object obj : groups) {
            JSONObject g = (JSONObject) obj;
            if (g.getString("groupName").equalsIgnoreCase(groupName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsByCode(String groupCode) {
        JSONArray groups = readGroupsFile();
        for (Object obj : groups) {
            JSONObject g = (JSONObject) obj;
            if (g.getString("groupCode").equals(groupCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveGroup(Group group) {
        JSONArray groups = readGroupsFile();
        groups.put(groupToJson(group));
        writeGroupsFile(groups);
    }

    @Override
    public boolean joinGroup(String groupCode, SpotifyUser loggedInUser) {
        JSONArray groups = readGroupsFile();

        for (int i = 0; i < groups.length(); i++) {
            JSONObject g = groups.getJSONObject(i);

            if (g.getString("groupCode").equals(groupCode)) {
                // Get users array
                JSONArray usersArray = g.optJSONArray("users");
                if (usersArray == null) {
                    usersArray = new JSONArray();
                    g.put("users", usersArray);
                }

                // Check if user is already in the group
                for (Object u : usersArray) {
                    if (u.toString().equals(loggedInUser.getUsername())) {
                        return false; // Already in group
                    }
                }

                // Add user
                JSONObject userJson = new JSONObject();
                userJson.put("username", loggedInUser.getUsername());
                userJson.put("accessToken", loggedInUser.getAccessToken());
                userJson.put("refreshToken", loggedInUser.getRefreshToken());
                userJson.put("spotifyUserId", loggedInUser.getSpotifyUserId());
                usersArray.put(userJson);

                // Write back to file
                writeGroupsFile(groups);
                return true; // Successfully joined
            }
        }

        return false; // Group not found
    }

    @Override
    public void updateGroup(Group group) {
        JSONArray groups = readGroupsFile();
        for (int i = 0; i < groups.length(); i++) {
            JSONObject g = groups.getJSONObject(i);
            if (g.getString("groupCode").equals(group.getGroupCode())) {
                groups.put(i, groupToJson(group));
                writeGroupsFile(groups);
                return;
            }
        }
    }

    // ------------------- Interface method -------------------
    @Override
    public Group getGroupByCode(String groupCode) {
        // Satisfies interface
        return getGroupByCode(groupCode, new ArrayList<>());
    }

    // ------------------- New method with logged-in users -------------------
    public Group getGroupByCode(String groupCode, List<SpotifyUser> loggedInUsers) {
        JSONArray groups = readGroupsFile();
        for (Object obj : groups) {
            JSONObject g = (JSONObject) obj;
            if (g.getString("groupCode").equals(groupCode)) {
                List<SpotifyUser> users = new ArrayList<>();

                JSONArray usersArray = g.optJSONArray("users");
                if (usersArray != null) {
                    for (Object uObj : usersArray) {
                        JSONObject userJson = (JSONObject) uObj;

                        String username = userJson.getString("username");
                        String spotifyUserId = userJson.optString("spotifyUserId");

                        // Try to load full user (with tokens)
                        SpotifyUser fullUser =
                                SpotifyUserDataAccessObject.getInstance().getUserById(spotifyUserId);

                        if (fullUser != null) {
                            users.add(fullUser);
                        } else {
                            // fallback to stored JSON
                            users.add(new SpotifyUser(
                                    username,
                                    userJson.optString("accessToken", null),
                                    userJson.optString("refreshToken", null),
                                    spotifyUserId
                            ));
                        }
                    }
                }

                return new Group(g.getString("groupName"), users);
            }
        }
        return null; // group code not found
    }

    // ------------------- Helpers -------------------

    private JSONArray readGroupsFile() {
        if (!groupFile.exists()) return new JSONArray();
        try (BufferedReader br = new BufferedReader(new FileReader(groupFile))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) content.append(line);
            if (content.isEmpty()) return new JSONArray();
            return new JSONArray(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private void writeGroupsFile(JSONArray groups) {
        try (FileWriter writer = new FileWriter(groupFile)) {
            writer.write(groups.toString(4)); // pretty print
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject groupToJson(Group group) {
        JSONObject json = new JSONObject();
        json.put("groupName", group.getGroup_name());
        json.put("groupCode", group.getGroupCode());

        JSONArray usersArray = new JSONArray();
        for (SpotifyUser user : group.getUsers()) {
            JSONObject userJson = new JSONObject();
            userJson.put("username", user.getUsername());
            userJson.put("accessToken", user.getAccessToken());
            userJson.put("refreshToken", user.getRefreshToken());
            userJson.put("spotifyUserId", user.getSpotifyUserId());
            usersArray.put(userJson);
        }
        json.put("users", usersArray);

        return json;
    }

    private Group jsonToGroup(JSONObject json) {
        JSONArray usersArray = json.optJSONArray("users");
        List<SpotifyUser> users = new ArrayList<>();

        if (usersArray != null) {
            for (Object u : usersArray) {
                JSONObject userJson = (JSONObject) u;
                users.add(new SpotifyUser(
                        userJson.getString("username"),
                        userJson.getString("accessToken"),
                        userJson.getString("refreshToken"),
                        userJson.getString("spotifyUserId")
                ));
            }
        }

        // Create group with users
        Group group = new Group(json.getString("groupName"), users);
        return group;
    }
}
