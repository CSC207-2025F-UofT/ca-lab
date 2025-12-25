package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdministratorTest {

    @Test
    public void testAdministrator() {
        Administrator a = new Administrator("admin", "admin12345");

        assertEquals("admin", a.getName());
        assertEquals("admin12345", a.getPassword());
        assertEquals("administrator", a.getUserType());
    }
}
