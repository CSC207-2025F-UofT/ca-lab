package entity;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple entity representing a user. Users have a username and password..
 */
public class User {

    private final String name;
    private final String password;
    private ArrayList<JSONObject> watchlist = new ArrayList<>();

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String name, String password) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<JSONObject> getWatchlist() { return watchlist; }

    public void setWatchlist(ArrayList<JSONObject> watchlist) {
        this.watchlist = watchlist;
    }

}
