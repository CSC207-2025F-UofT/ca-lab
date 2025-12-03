package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                // views
                .addGameDashboard()
                .addSettingsView()
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addAkinatorView()
                .addChooseFavPokemonView()
                .addLeaderboardView()
                .addUserProfileView()
                .addMysteryPokemonView()

                // use cases
                .addSignupUseCase()
                .addLoginUseCase()
                .addAkinatorUseCase()
                .addMysteryPokemonUseCase()
                .addChangePasswordUseCase()
                .addAccessSettingsUseCase()
                .addSaveSettingsUseCase()
                .addResetSettingsUseCase()
                .addChooseFavPokemonUseCase()
                .addLeaderboardUseCase()
                .addUserProfileUseCase()
                .build();


        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
