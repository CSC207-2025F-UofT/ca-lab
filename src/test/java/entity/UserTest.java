package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    public void testUser() {
        User u = new User("user", "user12345");

        assertEquals("user", u.getName());
        assertEquals("user12345", u.getPassword());
        assertEquals("user", u.getUserType());
    }

    @Test
    public void emptyNameTest() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new User("", "user12345")
        );
        assertEquals("Username cannot be empty", ex.getMessage());
    }

    @Test
    public void emptyPasswordTest() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new User("user", "")
        );
        assertEquals("Password cannot be empty", ex.getMessage());
    }
}
