package app;

import data_access.*;
import entity.SimplePokemonProfile;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonController;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonPresenter;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonViewModel;
import interface_adapter.user_profile.UserProfileController;
import interface_adapter.user_profile.UserProfilePresenter;
import interface_adapter.user_profile.UserProfileViewModel;
import interface_adapter.logged_in.*;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.mysterypokemon.MysteryPokemonController;
import interface_adapter.mysterypokemon.MysteryPokemonPresenter;
import interface_adapter.mysterypokemon.MysteryPokemonViewModel;
import interface_adapter.settings.*;
import interface_adapter.settings.apply.ApplySettingsController;
import interface_adapter.settings.apply.ApplySettingsPresenter;
import interface_adapter.back.BackController;
import interface_adapter.back.BackPresenter;
import interface_adapter.settings.reset.ResetSettingsController;
import interface_adapter.settings.reset.ResetSettingsPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.themes.ThemeManager;
import interface_adapter.leaderboard.*;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.choose_fav_pokemon.ChooseFavPokemonInputBoundary;
import use_case.choose_fav_pokemon.ChooseFavPokemonInteractor;
import use_case.choose_fav_pokemon.ChooseFavPokemonOutputBoundary;
import use_case.user_profile.*;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.settings.apply.ApplySettingsInputBoundary;
import use_case.settings.apply.ApplySettingsInteractor;
import use_case.settings.apply.ApplySettingsOutputBoundary;
import use_case.back.BackInputBoundary;
import use_case.back.BackInteractor;
import use_case.back.BackOutputBoundary;
import use_case.settings.reset.ResetSettingsInputBoundary;
import use_case.settings.reset.ResetSettingsInteractor;
import use_case.settings.reset.ResetSettingsOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.leaderboard.*;
import interface_adapter.akinator.AkinatorController;
import interface_adapter.akinator.AkinatorPresenter;
import interface_adapter.akinator.AkinatorViewModel;
import use_case.akinator.AkinatorInputBoundary;
import use_case.akinator.AkinatorInteractor;
import use_case.akinator.AkinatorOutputBoundary;
import use_case.mysterypokemon.MysteryPokemonInputBoundary;
import use_case.mysterypokemon.MysteryPokemonInteractor;
import use_case.mysterypokemon.MysteryPokemonOutputBoundary;
import view.*;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * A builder responsible for constructing and assembling all components of the
 * application, including controllers, use cases, data access objects, and views.
 * <p>
 * The AppBuilder centralizes the wiring of dependencies so that each layer
 * follows Clean Architecture principles. Callers configure the necessary
 * components using the builder's setup methods and finalize construction with
 * {@link #build()}.
 * </p>
 *
 * <p><strong>Typical usage:</strong></p>
 * <pre>{@code
 * AppBuilder builder = new AppBuilder();
 * builder.addUserDataAccess(new InMemoryUserDataAccess());
 * builder.addLoginUseCase();
 * builder.addLoginController();
 * App app = builder.build();
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final UserFactory userFactory = new UserFactory();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // DAO for user persistence
    private final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv", userFactory);

    private final Gen1Loader gen1loader = new Gen1Loader();
    private final TypeFetcher typeFetcher = new TypeFetcher();
    private final TypeMultiplierCalculator typeMultiplierCalculator = new TypeMultiplierCalculator(typeFetcher);
    private final GameDataAccessInterface gameDAO = new FileGameDataAccess("game_state.json");
    private final PokemonDataAccessInterface pokemonDAO = new PokemonFetcher();



    // Views and view models
    private GameDashboard gameDashboard;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoginView loginView;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private SettingsViewModel settingsViewModel;
    private SettingsView settingsView;
    private ChooseFavPokemonViewModel chooseFavPokemonViewModel;
    private ChooseFavPokemonView chooseFavPokemonView;
    private AkinatorViewModel akinatorViewModel;
    private AkinatorView akinatorView;
    private LeaderboardViewModel leaderboardViewModel;
    private LeaderboardView leaderboardView;
    private UserProfileViewModel userProfileViewModel;
    private UserProfileView userProfileView;
    private MysteryPokemonViewModel mysteryPokemonViewModel;
    private MysteryPokemonView mysteryPokemonView;
    private final ThemeManager themeManager = new ThemeManager();

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }


    // ========== Add Views ==========

    /**
     * Registers the dashboard view with the application.
     */
    public AppBuilder addGameDashboard() {
        gameDashboard = new GameDashboard(viewManagerModel, themeManager); // fixed: assign to field
        cardPanel.add(gameDashboard, gameDashboard.getViewName());

        // To change themes
        themeManager.registerView(gameDashboard);
        cardPanel.add(gameDashboard, gameDashboard.getViewName());

        return this;
    }

    /**
     * Registers the signup view with the application.
     */
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel, viewManagerModel, themeManager);
        cardPanel.add(signupView, signupView.getViewName());
        addAccessSettingsUseCase();
        return this;
    }

    /**
     * Registers the login view with the application.
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel, themeManager);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Registers the logged in view with the application.
     */
    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    /**
     * Registers the Akinator view with the application.
     */
    public AppBuilder addAkinatorView() {
        akinatorViewModel = new AkinatorViewModel();
        akinatorView = new AkinatorView(akinatorViewModel, viewManagerModel, themeManager);
        cardPanel.add(akinatorView, akinatorView.getViewName());
        themeManager.registerView(akinatorView);

        return this;
    }

    public AppBuilder addMysteryPokemonView() {
        mysteryPokemonViewModel = new MysteryPokemonViewModel();
        mysteryPokemonView = new MysteryPokemonView(gen1loader, mysteryPokemonViewModel, viewManagerModel);
        cardPanel.add(mysteryPokemonView, mysteryPokemonView.getViewName());
        return this;
    }

    /**
     * Registers the settings view with the application.
     */
    public AppBuilder addSettingsView() {
        settingsViewModel = new SettingsViewModel();
        settingsView = new SettingsView(settingsViewModel, themeManager);
        cardPanel.add(settingsView, settingsView.getViewName());
        themeManager.registerView(settingsView);
        return this;
    }

    /**
     * Registers the leaderboard view with the application.
     */
    public AppBuilder addLeaderboardView() {
        leaderboardViewModel = new LeaderboardViewModel();
        leaderboardView = new LeaderboardView(leaderboardViewModel, themeManager);
        cardPanel.add(leaderboardView, leaderboardView.getViewName());
        return this;
    }

    /**
     * Registers the choose favourite Pokémon view with the application.
     */
    public AppBuilder addChooseFavPokemonView() {
        chooseFavPokemonViewModel = new ChooseFavPokemonViewModel(
                userDataAccessObject.get(gameDashboard.getCurrentUser()));
        chooseFavPokemonView = new ChooseFavPokemonView(chooseFavPokemonViewModel, viewManagerModel, gameDashboard,
                userDataAccessObject, new PokeApiGateway(), themeManager);

        cardPanel.add(chooseFavPokemonView, chooseFavPokemonView.getViewName());
        return this;
    }

    /**
     * Registers the user profile view with the application.
     */
    public AppBuilder addUserProfileView() {
        // Initialize with a default user or null - will be updated when view is shown
        userProfileViewModel = new UserProfileViewModel();
        userProfileView = new UserProfileView(userProfileViewModel, viewManagerModel, gameDashboard,
                userDataAccessObject, new PokeApiGateway(), themeManager);

        cardPanel.add(userProfileView, userProfileView.getViewName());
        return this;
    }


    // ========== Add Use Cases ==========

    /**
     * Creates and registers the signup use case, including its interactor and presenter.
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor =
                new SignupInteractor(userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);

        return this;
    }

    /**
     * Creates and registers the login use case, including its interactor and presenter.
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary =
                new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel, gameDashboard);

        final LoginInputBoundary loginInteractor =
                new LoginInteractor(userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        addAccessSettingsUseCase();
        return this;
    }

    /**
     * Creates and registers the change password use case, including its interactor and presenter.
     */
    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary =
                new ChangePasswordPresenter(viewManagerModel, loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController =
                new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Creates and registers the Akinator use case, including its interactor and presenter.
     */
    public AppBuilder addAkinatorUseCase() {
        AkinatorOutputBoundary presenter = new AkinatorPresenter(akinatorViewModel);
        List<SimplePokemonProfile> dynamicProfiles = Collections.emptyList();
        try {
            dynamicProfiles = new AkinatorKnowledgeBaseLoader().load(151);
            System.out.println("Pokénator: loaded " + dynamicProfiles.size() + " Pokémon from PokéAPI.");
        } catch (IOException e) {
            System.err.println("Pokénator: falling back to default roster (" + e.getMessage() + ")");
        }
        AkinatorInputBoundary interactor =
                new AkinatorInteractor(presenter, new PokeApiGateway(), dynamicProfiles);
        AkinatorController controller = new AkinatorController(interactor);
        akinatorView.setController(controller);
        return this;
    }

    public AppBuilder addMysteryPokemonUseCase() {
        MysteryPokemonOutputBoundary presenter = new MysteryPokemonPresenter(mysteryPokemonViewModel, viewManagerModel);
        MysteryPokemonInputBoundary interactor = new MysteryPokemonInteractor(pokemonDAO, gameDAO, typeMultiplierCalculator, presenter);
        MysteryPokemonController controller = new MysteryPokemonController(interactor);
        mysteryPokemonView.setMysteryPokemonController(controller);
        return this;
    }

    /**
     * Creates and registers the reset settings use case, including its interactor and presenter.
     */
    public AppBuilder addResetSettingsUseCase() {

        ResetSettingsOutputBoundary presenter =
                new ResetSettingsPresenter(settingsViewModel, themeManager);

        ResetSettingsInputBoundary interactor =
                new ResetSettingsInteractor(presenter);

        ResetSettingsController controller =
                new ResetSettingsController(interactor);

        settingsView.setResetSettingsController(controller);
        return this;
    }

    /**
     * Creates and registers the access settings use case, including its interactor and presenter.
     */
    public AppBuilder addAccessSettingsUseCase() {

        BackOutputBoundary presenter =
                new BackPresenter(viewManagerModel, "dashboard");

        BackInputBoundary interactor =
                new BackInteractor(presenter);

        BackController controller =
                new BackController(interactor);

        settingsView.setAccessSettingsController(controller);
        loginView.setBackController(controller);
        signupView.setAccessSettingsController(controller);
        return this;
    }

    /**
     * Creates and registers the save settings use case, including its interactor and presenter.
     */
    public AppBuilder addSaveSettingsUseCase() {

        ApplySettingsOutputBoundary presenter =
                new ApplySettingsPresenter(viewManagerModel, settingsViewModel, themeManager);

        ApplySettingsInputBoundary interactor =
                new ApplySettingsInteractor(presenter);

        ApplySettingsController controller =
                new ApplySettingsController(interactor);

        settingsView.setSaveSettingsController(controller);
        return this;
    }

    /**
     * Creates and registers the choose favourite Pokémon use case, including its interactor and presenter.
     */
    public AppBuilder addChooseFavPokemonUseCase() {

        ChooseFavPokemonOutputBoundary presenter =
                new ChooseFavPokemonPresenter(chooseFavPokemonViewModel, viewManagerModel);

        ChooseFavPokemonInputBoundary interactor =
                new ChooseFavPokemonInteractor(userDataAccessObject, presenter, userFactory, gameDashboard);

        ChooseFavPokemonController controller = new ChooseFavPokemonController(interactor);

        chooseFavPokemonView.setChooseFavPokemonController(controller);
        return this;
    }

    /**
     * Creates and registers the leaderboard use case, including its interactor and presenter.
     */
    public AppBuilder addLeaderboardUseCase() {

        // Leaderboard:
        LeaderboardOutputBoundary leaderboardPresenter =
                new LeaderboardPresenter(leaderboardViewModel);
        LeaderboardInputBoundary leaderboardInteractor =
                new LeaderboardInteractor(userDataAccessObject, leaderboardPresenter);
        LeaderboardController leaderboardController =
                new LeaderboardController(leaderboardInteractor);

        // Back:
        BackOutputBoundary backPresenter =
                new BackPresenter(viewManagerModel, "dashboard");
        BackInputBoundary backInteractor =
                new BackInteractor(backPresenter);
        BackController backController =
                new BackController(backInteractor);

        leaderboardView.setControllers(leaderboardController, backController);

        // Preemptively set up page 1.
        leaderboardController.changePage(1);

        return this;
    }

    /**
     * Creates and registers the user profile use case, including its interactor and presenter.
     */
    public AppBuilder addUserProfileUseCase() {

        UserProfileOutputBoundary presenter =
                new UserProfilePresenter(userProfileViewModel, viewManagerModel);

        UserProfileInputBoundary interactor =
                new UserProfileInteractor(userDataAccessObject, presenter, userFactory, gameDashboard);

        UserProfileController controller = new UserProfileController(interactor);

        userProfileView.setUserProfileController(controller);
        return this;
    }


    // ========== Build Application ==========

    /**
     * Finalizes the application setup by assembling all registered views,
     * controllers, and use cases into a fully configured application instance.
     * This method should be called only after all required components have been
     * added through the builder's setup methods.
     *
     * @return the fully constructed and ready-to-run application
     */
    public JFrame build() {
        final JFrame application = new JFrame("Pokénator Dashboard");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        application.setSize(1200, 1000);
        application.setLocationRelativeTo(null); // Center window on screen

        // Start on Game Dashboard
        viewManagerModel.setState("dashboard");
        viewManagerModel.firePropertyChange();

        return application;
    }

}
