package app;
import javax.swing.JFrame;
import java.awt.CardLayout;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.DBApplicationDataAccessObject;
import data_access.DatabaseApplicationGateway;
import data_access.DatabaseConnection;
import data_access.DatabasePetGateway;
import data_access.DBUserDataAccessObject;
import data_access.PetAPIGatewayInterface;
import data_access.SQLitePetDataAccessObject;
import entity.PetFactory;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_pet.AddPetController;
import interface_adapter.add_pet.AddPetPageController;
import interface_adapter.add_pet.AddPetPresenter;
import interface_adapter.add_pet.AddPetView;
import interface_adapter.add_pet.AddPetViewModel;
import interface_adapter.browse_filter.BrowseFilterController;
import interface_adapter.browse_filter.BrowseFilterPageController;
import interface_adapter.browse_filter.BrowseFilterPresenter;
import interface_adapter.browse_filter.BrowseFilterViewModel;
import interface_adapter.delete_pet.*;
import interface_adapter.home.HomeViewModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.manage_application.*;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.submit_application.SubmitController;
import interface_adapter.submit_application.SubmitPresenter;
import interface_adapter.submit_application.SubmitViewModel;
import interface_adapter.view_pet_details.ViewPetDetailsController;
import interface_adapter.view_pet_details.ViewPetDetailsPresenter;
import interface_adapter.view_pet_details.ViewPetDetailsViewModel;
import use_case.add_pet.AddPetInputBoundary;
import use_case.add_pet.AddPetInteractor;
import use_case.add_pet.AddPetOutputBoundary;
import use_case.browse_filter.BrowseFilterInputBoundary;
import use_case.browse_filter.BrowseFilterInteractor;
import use_case.browse_filter.BrowseFilterOutputBoundary;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
//import use_case.login.LoginInputBoundary; it wasn't working, did login not have input boundary?
import use_case.delete_pet.DeletePetInputBoundary;
import use_case.delete_pet.DeletePetInteractor;
import use_case.delete_pet.DeletePetOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.manage_application.ManageApplicationInteractor;
import use_case.manage_application.ManageApplicationOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.submit_application.SubmitInputBoundary;
import use_case.submit_application.SubmitInteractor;
import use_case.submit_application.SubmitOutputBoundary;
import use_case.view_pet_details.ViewPetDetailsInputBoundary;
import use_case.view_pet_details.ViewPetDetailsInteractor;
import use_case.view_pet_details.ViewPetDetailsOutputBoundary;
import view.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    //final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);

    // DAO version using a shared external database
    final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);


    // Application DAO using a database
    final Connection connection;
    final DatabaseApplicationGateway  databaseApplicationGateway;
    final DBApplicationDataAccessObject applicationDataAccessObject;

    private BrowseFilterView browseFilterView;
    private PetAPIGatewayInterface petGateway;
    private BrowseFilterPresenter browseFilterPresenter;
    private ViewPetDetailsViewModel viewPetDetailsViewModel;
    private ViewPetDetailsPresenter viewPetDetailsPresenter;
    private ViewPetDetailsView viewPetDetailsView;
    private ViewPetDetailsController viewPetDetailsController;
    private BrowseFilterViewModel browseFilterViewModel;
    private SubmitView submitView;
    private SubmitViewModel submitViewModel;
    private SubmitController submitController;
    private ManageApplicationView manageApplicationView;
    private ManageApplicationViewModel manageApplicationViewModel;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private AddPetViewModel addPetViewModel;
    private DeletePetViewModel deletePetViewModel;
    private HomeViewModel homeViewModel;
    private HomeView homeView;
    private ManageApplicationController manageApplicationController;
    private ManageApplicationInteractor manageApplicationInteractor;
    private AddPetView addPetView;
    private DeletePetView deletePetView;
    private AddPetController addPetController;
    private DeletePetController deletePetController;
    private SQLitePetDataAccessObject petRepo = new SQLitePetDataAccessObject();

    public AppBuilder() {
        try {
            this.connection = DatabaseConnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.databaseApplicationGateway = new DatabaseApplicationGateway(connection);
        this.applicationDataAccessObject = new DBApplicationDataAccessObject(databaseApplicationGateway);
        this.petGateway = new DatabasePetGateway(connection);

        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSubmitApplicationView() {
        submitViewModel = new SubmitViewModel();
        submitView = new SubmitView(submitViewModel);
        cardPanel.add(submitView, submitView.getViewName());
        return this;
    }

    public AppBuilder addViewPetDetails() {
        viewPetDetailsViewModel = new ViewPetDetailsViewModel();
        viewPetDetailsView = new ViewPetDetailsView(viewPetDetailsViewModel);
        return this;
    }

    public AppBuilder addViewPetDetailsUseCase() {
        final ViewPetDetailsOutputBoundary viewPetDetailsOutputBoundary = new ViewPetDetailsPresenter(viewPetDetailsViewModel, viewManagerModel, submitViewModel);
        final ViewPetDetailsInputBoundary viewPetDetailsInputBoundary = new ViewPetDetailsInteractor(petGateway, viewPetDetailsOutputBoundary);

        this.viewPetDetailsController = new ViewPetDetailsController(viewPetDetailsInputBoundary);
        viewPetDetailsView.setViewPetDetailsController(viewPetDetailsController);
        return this;
    }

    public AppBuilder addBrowseFilterView() {
        browseFilterViewModel = new BrowseFilterViewModel();
        browseFilterView =
                new BrowseFilterView(browseFilterViewModel, viewPetDetailsViewModel, viewManagerModel);
        cardPanel.add(browseFilterView, browseFilterView.getViewName());
        return this;
    }



    public AppBuilder addManageApplicationView() {

        // 1. ViewModel
        manageApplicationViewModel = new ManageApplicationViewModel();

        // 2. Presenter
        ManageApplicationOutputBoundary presenter =
                new ManageApplicationPresenter(manageApplicationViewModel);

        // 3. Interactor (DAO already exists!)
        ManageApplicationInteractor interactor =
                new ManageApplicationInteractor(applicationDataAccessObject, presenter);

        // 4. Controller
        manageApplicationController =
                new ManageApplicationController(interactor);


        // 5. View (controller is NOT null now)
        manageApplicationView =
                new ManageApplicationView(manageApplicationController, manageApplicationViewModel);

        manageApplicationView.setViewManagerModel(viewManagerModel);


        cardPanel.add(manageApplicationView, manageApplicationView.getViewName());
        return this;
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addHomeView() {
        homeViewModel = new HomeViewModel();
//        loggedInViewModel = new LoggedInViewModel(); // needed for role detection

        // Create page-opening controllers (no input required)
        BrowseFilterPageController browseFilterPageController =
                new BrowseFilterPageController(viewManagerModel, browseFilterViewModel);
        AddPetPageController addPetPageController = new AddPetPageController(viewManagerModel, addPetViewModel);
        DeletePetPageController deletePetPageController = new DeletePetPageController(viewManagerModel, deletePetViewModel);
        ManageApplicationsPageController manageApplicationsPageController =
                new ManageApplicationsPageController(viewManagerModel, manageApplicationViewModel);
        final LogoutOutputBoundary logoutOutputBoundary =
                new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);
        final LogoutController logoutController =
                new LogoutController(logoutInteractor);

        // Create the actual HomeView
        homeView = new HomeView(
                loggedInViewModel,
                browseFilterPageController,
                addPetPageController,
                deletePetPageController,
                manageApplicationsPageController,
                logoutController
        );

        cardPanel.add(homeView, "home");
        return this;
    }

    //for use case
    public AppBuilder addSubmitApplicationUseCase() {
        final SubmitOutputBoundary submitOutputBoundary =
                new SubmitPresenter(viewManagerModel, submitViewModel, browseFilterViewModel);
        final SubmitInputBoundary submitInputBoundary =
                new SubmitInteractor(userDataAccessObject, applicationDataAccessObject, submitOutputBoundary);

        this.submitController = new SubmitController(submitInputBoundary);
        submitView.setSubmitController(submitController);
        viewPetDetailsView.setSubmitController(submitController);
        viewPetDetailsView.setSubmitViewModel(submitViewModel);
        return this;
    }



    public AppBuilder addBrowseFilterUseCase() {
        final BrowseFilterOutputBoundary browseFilterOutputBoundary = new BrowseFilterPresenter(browseFilterViewModel);
        final BrowseFilterInputBoundary browseFilterInputBoundary =
                new BrowseFilterInteractor(petGateway, browseFilterOutputBoundary);

        BrowseFilterController browseFilterController = new BrowseFilterController(browseFilterInputBoundary);
        browseFilterView.setBrowseFilterController(browseFilterController);
        browseFilterView.setDetailsController(viewPetDetailsController);
        browseFilterView.setViewPetDetailsView(viewPetDetailsView);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    public AppBuilder addAddPetView() {
        this.addPetView = new AddPetView();
        cardPanel.add(addPetView, addPetView.getViewName());
        return this;
    }

    public AppBuilder addDeletePetView() {
        this.deletePetView = new DeletePetView();
        cardPanel.add(deletePetView, deletePetView.getViewName());
        return this;
    }

    public AppBuilder addAddPetUseCase() {

        this.addPetViewModel = new AddPetViewModel();
        AddPetOutputBoundary presenter = new AddPetPresenter(addPetViewModel);

        PetFactory factory = new PetFactory();

        AddPetInputBoundary interactor =
                new AddPetInteractor(factory, petRepo, presenter);

        this.addPetController = new AddPetController(interactor);
        this.addPetView.setViewModel(addPetViewModel);
        this.addPetView.setAddPetController(addPetController);

        return this;
    }

    public AppBuilder addDeletePetUseCase() {

        this.deletePetViewModel = new DeletePetViewModel();
        DeletePetOutputBoundary presenter = new DeletePetPresenter(deletePetViewModel);

        DeletePetInputBoundary interactor =
                new DeletePetInteractor(petRepo, presenter);

        this.deletePetController = new DeletePetController(interactor);

        this.deletePetView.setViewModel(deletePetViewModel);
        this.deletePetView.setDeletePetController(deletePetController);

        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }

}


