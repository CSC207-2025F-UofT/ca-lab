package use_case.sharedsong;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.InGroupViewModel;
import interface_adapter.create_group.UserGroupState;
import interface_adapter.group_analytics.GroupAnalyticsViewModel;
import interface_adapter.sharedsong.SharedSongState;
import interface_adapter.sharedsong.SharedSongViewModel;
import view.InGroupView;
import view.SharedSongView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedSongViewError {
    public static void main (String[] args) {

        SharedSongState state = new SharedSongState();
        UserGroupState groupState = new UserGroupState();
        groupState.setGroupUsernames(new ArrayList<>(List.of(new String[]{"I", "Want", "To", "Sleep"})));
        groupState.setGroupName("tired");
        InGroupViewModel viewModel = new InGroupViewModel();
        SharedSongViewModel sharedSongViewModel = new SharedSongViewModel();
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        Map<String,String> testMap = new HashMap<String,String>();
        testMap.put("user1", "No");
        testMap.put("user2", "Yes");
        testMap.put("user3", "Yes");
        testMap.put("user4", "No");

        state.setUsernameToShared(testMap);
        state.setErrorMessage("try playing a song");
        sharedSongViewModel.setState(state);
        viewModel.setState(groupState);

        SharedSongView view = new SharedSongView(viewModel, sharedSongViewModel, viewManagerModel);

        InGroupView ingroupview = new InGroupView(viewManagerModel, viewModel, sharedSongViewModel,
                new GroupAnalyticsViewModel());

        JPanel cardPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
        cardPanel.setLayout(cardLayout);
        cardPanel.add(view, view.getViewName());
        cardPanel.add(ingroupview, ingroupview.getViewName());

        JFrame test = new JFrame("sharedsong view error test");
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.add(cardPanel);
        viewManagerModel.setState(ingroupview.getViewName());
        viewManagerModel.firePropertyChange();
        test.setSize(800, 600);
        test.setVisible(true);
        viewModel.firePropertyChange("shared song error");
        JOptionPane.showMessageDialog(test,
                sharedSongViewModel.getState().getErrorMessage(),
                "Shared Song Error",
                JOptionPane.WARNING_MESSAGE);



    }
}
