package app;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addLoggedInView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .build();

        application.pack();
        application.setVisible(true);
    }
}