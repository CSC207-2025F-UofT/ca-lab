package use_case.loyaltyscore;

import interface_adapter.ViewManagerModel;
import interface_adapter.loyalty_score.LoyaltyState;
import interface_adapter.loyalty_score.LoyaltyViewModel;
import view.LoyaltyScoreView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoyaltyScoreViewTest {
    public static void main(String[] args) {

        // Create the LoyaltyState and ViewManagerModel
        LoyaltyState loyaltyState = new LoyaltyState();

        loyaltyState.setCurrentArtist("Phoebe Bridgers");

        ArrayList<String> dates = new ArrayList<>(List.of("2025-04-12","2025-04-13",
                "2025-04-14", "2023-08-09", "2025-04-15"));

        loyaltyState.setDates(dates);

        System.out.println(loyaltyState.getDates());
        loyaltyState.setLoyaltyScores(Map.of(
                "2025-04-12", 90,
                "2025-04-13", 140,
                "2025-04-14", 85,
                "2023-08-09", 12
        ));

        ViewManagerModel viewManagerModel = new ViewManagerModel();

        LoyaltyViewModel loyaltyViewModel = new LoyaltyViewModel();

        loyaltyViewModel.setState(loyaltyState);

        LoyaltyScoreView loyaltyScoreView = new LoyaltyScoreView(loyaltyViewModel, viewManagerModel);

        // Add to the frame
        JFrame frame = new JFrame("Loyalty Scores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(loyaltyScoreView);
        frame.setSize(800, 600);
        frame.setVisible(true);

    }
}