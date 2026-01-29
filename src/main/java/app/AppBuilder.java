package app;

import data_access.FileUserDataAccessObject;
import data_access.SpotifyDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.spotify_auth.SpotifyAuthController;
import interface_adapter.spotify_auth.SpotifyAuthPresenter;
import interface_adapter.spotify_auth.SpotifyAuthViewModel;
import interface_adapter.daily_mix.DailyMixViewModel;
import interface_adapter.daily_mix.DailyMixController;
import interface_adapter.daily_mix.DailyMixPresenter;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.spotify_auth.SpotifyAuthInputBoundary;
import use_case.spotify_auth.SpotifyAuthInteractor;
import use_case.spotify_auth.SpotifyAuthOutputBoundary;
import use_case.daily_mix.DailyMixInputBoundary;
import use_case.daily_mix.DailyMixInputData;
import use_case.daily_mix.DailyMixInteractor;
import use_case.daily_mix.DailyMixOutputBoundary;
import use_case.daily_mix.DailyMixOutputData;
import view.LoggedInView;
import view.LoginView;
import view.SpotifyAuthView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private SpotifyAuthView spotifyAuthView;
    private SpotifyAuthViewModel spotifyAuthViewModel;
    private DailyMixViewModel dailyMixViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addViewModels() {
        loginViewModel = new LoginViewModel();
        loggedInViewModel = new LoggedInViewModel();
        spotifyAuthViewModel = new SpotifyAuthViewModel();
        dailyMixViewModel = new DailyMixViewModel();
        return this;
    }

    public AppBuilder addLoginView() {
        loginView = new LoginView(loginViewModel, viewManagerModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInView = new LoggedInView(loggedInViewModel, viewManagerModel, spotifyAuthViewModel,dailyMixViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addSpotifyAuthView() {
        spotifyAuthView = new SpotifyAuthView(spotifyAuthViewModel, loggedInViewModel);  // Pass loggedInViewModel
        spotifyAuthView.setViewManagerModel(viewManagerModel);
        cardPanel.add(spotifyAuthView, spotifyAuthView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        // emptied; do not need
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        // final LoginInputBoundary loginInteractor = new LoginInteractor(
        //        userDataAccessObject, loginOutputBoundary);
        // TODO: add back in LoginInputBoundary loginInteractor variable


        // LoginController loginController = new LoginController(loginInteractor);
        // TODO: Add back in LoginController
        // loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        // emptied, do not need.
        return this;
    }

    public AppBuilder addLogoutUseCase() {
        // emptied; not necessary.
        return this;
    }

    public AppBuilder addSpotifyAuthUseCase() {
        // PKCE doesn't need client secret!
        data_access.SpotifyDataAccessObject spotifyDAO = new data_access.SpotifyDataAccessObject();

        final SpotifyAuthOutputBoundary spotifyAuthOutputBoundary =
                new SpotifyAuthPresenter(viewManagerModel, spotifyAuthViewModel, loggedInViewModel);

        if (spotifyAuthOutputBoundary instanceof SpotifyAuthPresenter) {
            ((SpotifyAuthPresenter) spotifyAuthOutputBoundary).setLoggedInView(loggedInView);
        }

        final SpotifyAuthInputBoundary spotifyAuthInteractor =
                new SpotifyAuthInteractor(spotifyDAO, spotifyAuthOutputBoundary);

        SpotifyAuthController controller = new SpotifyAuthController(spotifyAuthInteractor);
        spotifyAuthView.setSpotifyAuthController(controller);

        return this;
    }

    public AppBuilder addDailyMixUseCase() {
        // use new DailyMixPresenter and Interactor
        final DailyMixOutputBoundary dailyMixOutputBoundary =
                new DailyMixPresenter(dailyMixViewModel);

        final DailyMixInputBoundary dailyMixInteractor =
                new DailyMixInteractor(new SpotifyDataAccessObject(), dailyMixOutputBoundary);

        DailyMixController dailyMixController = new DailyMixController(dailyMixInteractor);
        loggedInView.setDailyMixController(dailyMixController);

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Better Wrapped - Spotify Analysis");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
