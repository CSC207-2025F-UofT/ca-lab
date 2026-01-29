package use_case.logged_in;

import javax.swing.*;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.spotify_auth.SpotifyAuthViewModel;
import interface_adapter.daily_mix.DailyMixViewModel;
import interface_adapter.get_topItems.GetTopItemsViewModel;
import view.LoggedInView;

public class LoggedInViewTest {

    public static void main(String[] args) {

        LoggedInViewModel loggedInVM = new LoggedInViewModel();
        SpotifyAuthViewModel spotifyAuthVM = new SpotifyAuthViewModel();
        DailyMixViewModel dailyMixVM = new DailyMixViewModel();
        GetTopItemsViewModel topItemsVM = new GetTopItemsViewModel();
        ViewManagerModel managerModel = new ViewManagerModel();

        LoggedInState state =  new LoggedInState();
        state.setSpotifyAuthenticated(true);

        loggedInVM.setState(state);

        // --- Create the view ---
        LoggedInView view = new LoggedInView(
                loggedInVM,
                managerModel,
                spotifyAuthVM,
                dailyMixVM,
                topItemsVM
        );

        // --- Wrap inside a frame ---
        JFrame frame = new JFrame("UI Test: LoggedInView");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // UI will NOT!! work b/c it always shows view
        frame.getContentPane().add(view);
        frame.pack();
        frame.setLocationRelativeTo(null);  // Center the frame on the screen
        frame.setVisible(true);

    }
}
