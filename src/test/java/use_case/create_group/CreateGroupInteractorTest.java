package use_case.create_group;
import data_access.FakeGroupDAO;
import entity.Group;
import entity.SpotifyUser;
import org.junit.jupiter.api.Test;
import use_case.create_group.CreateGroupInputData;
import use_case.create_group.CreateGroupInteractor;
import use_case.create_group.CreateGroupOutputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGroupInteractorTest {

    @Test
    void testCreateGroupSuccess() {
        FakeGroupDAO dao = new FakeGroupDAO();
        CreateGroupInteractor interactor = new CreateGroupInteractor(dao);

        SpotifyUser u = new SpotifyUser("Sam", "A", "R", "ID1");

        CreateGroupInputData input = new CreateGroupInputData(
                "MyGroup",
                u,
                List.of(u)
        );

        CreateGroupOutputData output = interactor.execute(input);

        // DAO should have actually stored group
        assertEquals(1, dao.savedGroups.size());

        Group g = dao.savedGroups.get(0);

        assertEquals("MyGroup", g.getGroup_name());
        assertEquals(1, g.getUsers().size());
        assertEquals("Sam", g.getUsers().get(0).getUsername());

        assertEquals("MyGroup", output.getGroup_name());
        assertEquals(1, output.getUsers().size());
    }

    @Test
    void testCreateGroupRejectsTooManyMembers() {
        SpotifyUser owner = new SpotifyUser("Sam", "A","R","ID");

        List<SpotifyUser> tooMany = List.of(
                owner, owner, owner, owner, owner, owner, owner, owner
        );

        assertThrows(IllegalArgumentException.class, () ->
                new CreateGroupInputData("abc", owner, tooMany)
        );
    }

    @Test
    void testCreateGroupRejectsBlankName() {
        SpotifyUser u = new SpotifyUser("Sam", "A","R","ID");

        assertThrows(IllegalArgumentException.class, () ->
                new CreateGroupInputData("", u, List.of(u))
        );
    }
}
