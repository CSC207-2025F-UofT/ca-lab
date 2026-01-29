package entity;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple entity representing a group. Groups have a group_name, group_playlists, users,
 * and date_created
 */
public class Group {

    public static final int MAX_MEMBERS = 7;
    private String group_name;
    private final List<SpotifyUser> users;
    private final List<Playlist> group_playlists;
    private final String groupCode;



    /**
     * Creates a new group given these parameters:
     *
     * @param group_name the group's name
     * @param users the users who are in the group
     */
    public Group(String group_name, List<SpotifyUser> users) {
        this.group_name = group_name;
        this.users = new ArrayList<>();
        if (users != null) {
            this.users.addAll(users);
        }
        this.group_playlists = new ArrayList<>();
        this.groupCode = generateGroupCode();
    }

    public String getGroup_name() {
        return this.group_name;
    }
    public String getGroupCode() {
        return groupCode;
    }


    public void addUser(SpotifyUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (users.size() >= MAX_MEMBERS) {
            throw new IllegalStateException("Cannot have more than 7 members in group");
        }
        users.add(user);
    }

    public void addPlaylist(Playlist playlist) { this.group_playlists.add(playlist); }

    public void setGroup_name(String group_name) {
        if (group_name == null || group_name.isBlank()){
            throw new IllegalArgumentException("Group name cannot be empty.");
        } this.group_name = group_name;
    }


    public List<SpotifyUser> getUsers() {
        return this.users;
    }

    private String generateGroupCode() {
        SecureRandom rand = new SecureRandom();
        int code = rand.nextInt(900000) + 100000; // random 6-digit codes associated with each group
        return String.valueOf(code);
    }
    public void removeUser(SpotifyUser user) {
        users.remove(user);
    }
}