package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(String type, String name, String password) {
        switch (type.toLowerCase()) {
            case "visitor":
                return new Visitor(name, password);
            case "administrator":
                return new Administrator(name, password);
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}
