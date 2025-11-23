package entity;

public class Administrator extends User{
    /**
     * Creates a new user with the given non-empty name and non-empty password.
     *
     * @param name     the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public Administrator(String name, String password) {
        super(name, password);
    }
}
