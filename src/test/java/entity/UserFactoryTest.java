package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserFactoryTest {

    @Test
    public void testCreateVisitor() {
        UserFactory userFactory = new UserFactory();
        User user = userFactory.create("visitor", "visitor1", "visitor12345");

        assertTrue(user instanceof Visitor);
        assertEquals("visitor1", user.getName());
        assertEquals("visitor12345", user.getPassword());
        assertEquals("visitor", user.getUserType());
    }

    @Test
    public void testCreateAdmin() {
        UserFactory userFactory = new UserFactory();
        User user = userFactory.create("administrator", "admin1", "admin12345");

        assertTrue(user instanceof Administrator);
        assertEquals("admin1", user.getName());
        assertEquals("admin12345", user.getPassword());
        assertEquals("administrator", user.getUserType());
    }

    @Test
    public void testCaseSensitive() {
        UserFactory userFactory = new UserFactory();
        User user = userFactory.create("Administrator", "admin1", "admin12345");

        assertTrue(user instanceof Administrator);
        assertEquals("administrator", user.getUserType());
    }

    @Test
    public void testCreateUnknown() {
        UserFactory userFactory = new UserFactory();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userFactory.create("Admin", "admin1", "admin12345")
        );

        assertEquals("Unknown user type: Admin", ex.getMessage());
    }
}
