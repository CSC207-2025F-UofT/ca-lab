package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addFilterSearchView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addFilterSearchUseCase()
                .addNewsView()
                .addEarningsHistoryView()
                .addTradeView()
                .addStockSearchUseCase()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addNewsUsecase()
                .addEarningsHistoryUseCase()
                .addAccount()
                .addMarketStatusUseCase()
                .addRealtimeTradeUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}