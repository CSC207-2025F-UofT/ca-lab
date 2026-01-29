package use_case.create_group;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.*;
import org.junit.jupiter.api.Test;
import use_case.create_group.CreateGroupOutputData;

import entity.Group;
import entity.SpotifyUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGroupPresenterTest {

    @Test
    void testPresenterUpdatesViewModels() {
        InGroupViewModel in = new InGroupViewModel();
        NoGroupViewModel no = new NoGroupViewModel();
        ViewManagerModel vm = new ViewManagerModel();

        CreateGroupPresenter presenter = new CreateGroupPresenter(in, no, vm);

        SpotifyUser u = new SpotifyUser("S", "A","R","ID");
        Group g = new Group("GG", List.of(u));

        CreateGroupOutputData out = new CreateGroupOutputData("GG", List.of(u), g);

        presenter.present(out);

        assertEquals("in group", vm.getState());
        assertEquals("GG", in.getState().getGroupName());
    }
}