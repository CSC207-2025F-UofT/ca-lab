package app;

import interface_adapter.loyalty_score.LoyaltyController;
import view.LoyaltyScoreView;

import javax.swing.*;

public class Main {
    // VARIABLES CLIENT_ID AND CLIENT_SECRET MUST BE SET:

    String CLIENT_ID;
    String CLIENT_SECRET;

    public static void main(String[] args) {
        String clientId = System.getenv("SPOTIFY_CLIENT_ID");

        if (clientId == null) {
            System.out.println("Please set SPOTIFY_CLIENT_ID environment variable");
            System.out.println("You can get this from: https://developer.spotify.com/dashboard");
            System.out.println("Note: PKCE flow doesn't require a client secret!");
            System.out.println("Running without Spotify integration...");
        }

        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addViewModels()         // ✅ Create ALL view models FIRST
                // not needed .addLoginView()
                .addLoyaltyView()
                .addLoggedInView()
                .addSpotifyAuthView()
                .addInGroupView()
                .addNoGroupView()
                .addSharedSongView()
                .addGroupAnalyticsView()

                .addSpotifyAuthUseCase()
                .addCreateGroupUseCase()
                .addDailyMixUseCase()
                .addGetTopItemsUseCase()
                .addLoyaltyUseCase()
                .addSharedSongUseCase()
                .addGroupAnalyticsUseCase()
                .build();
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);

    }
}