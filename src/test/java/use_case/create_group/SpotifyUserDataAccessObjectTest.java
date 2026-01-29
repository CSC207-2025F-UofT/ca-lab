package use_case.create_group;

import data_access.SpotifyUserDataAccessObject;
import entity.SpotifyUser;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class SpotifyUserDataAccessObjectTest {

    @Test
    void testAddAndLoad() {
        File temp = new File("temp_users.json");
        temp.delete(); // clean start

        SpotifyUserDataAccessObject dao = SpotifyUserDataAccessObject.getInstance();
        SpotifyUser u = new SpotifyUser("Sam","A","R","ID1");

        dao.addUser(u);

        assertNotNull(dao.getUserById("ID1"));
        assertEquals("Sam", dao.getUserById("ID1").getUsername());
    }
}