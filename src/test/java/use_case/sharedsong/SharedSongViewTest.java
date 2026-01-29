package use_case.sharedsong;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.InGroupViewModel;
import interface_adapter.sharedsong.SharedSongState;
import interface_adapter.sharedsong.SharedSongViewModel;
import view.SharedSongView;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class SharedSongViewTest {
    public static void main (String[] args) {

        SharedSongState state = new SharedSongState();
        InGroupViewModel viewModel = new InGroupViewModel();
        SharedSongViewModel sharedSongViewModel = new SharedSongViewModel();
        ViewManagerModel viewManagerModel = new ViewManagerModel();

        Map<String,String> testMap = new HashMap<String,String>();
        testMap.put("user1", "No");
        testMap.put("user2", "Yes");
        testMap.put("user3", "Yes");
        testMap.put("user4", "No");

        state.setUsernameToShared(testMap);
        sharedSongViewModel.setState(state);
        SharedSongView view = new SharedSongView(viewModel, sharedSongViewModel, viewManagerModel);

        JFrame test = new JFrame("sharedsong view test");
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.add(view);
        test.setSize(800, 600);
        test.setVisible(true);


    }
}
