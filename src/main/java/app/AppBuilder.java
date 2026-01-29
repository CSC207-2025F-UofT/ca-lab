package app;

import data_access.FinnhubEarningsDataAccessObject;
import interface_adapter.account.AccountController;
import interface_adapter.account.AccountPresenter;
import interface_adapter.account.AccountViewModel;
import interface_adapter.earnings_history.EarningsHistoryController;
import interface_adapter.earnings_history.EarningsHistoryPresenter;
import interface_adapter.earnings_history.EarningsHistoryViewModel;
import use_case.earnings_history.EarningsDataAccessInterface;
import use_case.earnings_history.GetEarningsHistoryInputBoundary;
import use_case.earnings_history.GetEarningsHistoryInteractor;
import use_case.earnings_history.GetEarningsHistoryOutputBoundary;
import use_case.watchlist.*;
import view.EarningsHistoryView;

import data_access.FileUserDataAccessObject;
import data_access.FilterSearchDataAccessObject;
import data_access.stock_search.FinnhubStockSearchDataAccessObject;
import data_access.NewsDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.filter_search.FilterSearchController;
import interface_adapter.filter_search.FilterSearchPresenter;
import interface_adapter.filter_search.FilterSearchViewModel;
import interface_adapter.stock_search.StockSearchController;
import interface_adapter.stock_search.StockSearchPresenter;
import interface_adapter.stock_search.StockSearchViewModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.news.NewsController;
import interface_adapter.news.NewsPresenter;
import interface_adapter.news.NewsViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.filter_search.FilterSearchDataAccessInterface;
import use_case.filter_search.FilterSearchInputBoundary;
import use_case.filter_search.FilterSearchInteractor;
import use_case.filter_search.FilterSearchOutputBoundary;
import use_case.stock_search.StockSearchDataAccessInterface;
import use_case.stock_search.StockSearchInputBoundary;
import use_case.stock_search.StockSearchInteractor;
import use_case.stock_search.StockSearchOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.news.NewsInputBoundary;
import use_case.news.NewsInteractor;
import use_case.news.NewsOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import interface_adapter.market_status.MarketStatusViewModel;
import interface_adapter.market_status.MarketStatusPresenter;
import interface_adapter.market_status.MarketStatusController;
import use_case.market_status.MarketStatusInputBoundary;
import use_case.market_status.MarketStatusInteractor;
import use_case.market_status.MarketStatusOutputBoundary;
import use_case.market_status.MarketStatusDataAccessInterface;
import data_access.MarketStatusDataAccessObject;
import data_access.FinnhubTradeDataAccessObject;
import use_case.trade.TradeDataAccessInterface;
import use_case.trade.TradeInputBoundary;
import use_case.trade.TradeInteractor;
import use_case.trade.TradeOutputBoundary;
import interface_adapter.trade.TradeController;
import interface_adapter.trade.TradePresenter;
import interface_adapter.trade.TradeViewModel;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // This key should be retrieved from the environment
    String apiKey = "d4lpdgpr01qr851prp30d4lpdgpr01qr851prp3g";

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);
    // DAO for earnings history
    final EarningsDataAccessInterface earningsDataAccessObject =
            new FinnhubEarningsDataAccessObject();
    final WatchlistUserDataAccessInterface watchlistDataAccessObject = userDataAccessObject;
    final FilterSearchDataAccessInterface filterSearchDataAccessObject =
            new FilterSearchDataAccessObject(apiKey);
    final StockSearchDataAccessInterface stockSearchDataAccessObject =
            new FinnhubStockSearchDataAccessObject(apiKey);
    final NewsDataAccessObject newsDataAccessObject = new NewsDataAccessObject(apiKey);
    final MarketStatusDataAccessInterface marketStatusDataAccessObject =
            new MarketStatusDataAccessObject(apiKey);

    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private FilterSearchViewModel filterSearchViewModel;
    private FilterSearchView filterSearchView;
    private StockSearchViewModel stockSearchViewModel;
    private StockSearchController stockSearchController;
    private NewsViewModel newsViewModel;
    private NewsView newsView;
    private EarningsHistoryViewModel earningsHistoryViewModel;
    private EarningsHistoryView earningsHistoryView;
    private AccountView accountView;
    private AccountViewModel accountViewModel;
    private TradeView tradeView;
    private TradeViewModel tradeViewModel;

    private MarketStatusViewModel marketStatusViewModel;
    private MarketStatusController marketStatusController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
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

    public AppBuilder addFilterSearchView() {
        filterSearchViewModel = new FilterSearchViewModel();
        filterSearchView = new FilterSearchView(filterSearchViewModel);
        cardPanel.add(filterSearchView, filterSearchView.getViewName());
        return this;
    }

    public AppBuilder addNewsView() {
        newsViewModel = new NewsViewModel();
        NewsOutputBoundary newsOutputBoundary = new NewsPresenter(newsViewModel);
        NewsInputBoundary newsInputBoundary =
                new NewsInteractor(newsDataAccessObject, newsOutputBoundary);
        NewsController newsController = new NewsController(newsInputBoundary);

        newsView = new NewsView(newsController, newsViewModel);
        cardPanel.add(newsView, newsView.getViewName());
        return this;
    }
    public AppBuilder addEarningsHistoryView() {
        // ViewModel
        earningsHistoryViewModel = new EarningsHistoryViewModel();

        // Presenter + interactor
        GetEarningsHistoryOutputBoundary outputBoundary =
                new EarningsHistoryPresenter(earningsHistoryViewModel);
        GetEarningsHistoryInputBoundary interactor =
                new GetEarningsHistoryInteractor(earningsDataAccessObject, outputBoundary);

        // Controller
        EarningsHistoryController controller =
                new EarningsHistoryController(interactor, earningsHistoryViewModel);

        // Swing view
        earningsHistoryView = new EarningsHistoryView(controller, earningsHistoryViewModel);

        cardPanel.add(earningsHistoryView, earningsHistoryView.getViewName());
        return this;
    }

    public AppBuilder addTradeView() {
        tradeViewModel = new TradeViewModel();

        TradeOutputBoundary tradeOutputBoundary = new TradePresenter(tradeViewModel);
        TradeDataAccessInterface tradeDataAccess = new FinnhubTradeDataAccessObject();
        TradeInputBoundary tradeInteractor = new TradeInteractor(tradeDataAccess, tradeOutputBoundary);
        TradeController tradeController = new TradeController(tradeInteractor);
        

        tradeView = new TradeView(tradeController, tradeViewModel);
        cardPanel.add(tradeView, tradeView.getViewName());
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
                loggedInViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);

        loginView.setBackNavigation(viewManagerModel, signupView.getViewName());
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

    public AppBuilder addFilterSearchUseCase() {
        final FilterSearchOutputBoundary filterSearchOutputBoundary = new FilterSearchPresenter(filterSearchViewModel);
        final FilterSearchInputBoundary filterSearchInteractor =
                new FilterSearchInteractor(filterSearchDataAccessObject, filterSearchOutputBoundary);

        FilterSearchController filterSearchController = new FilterSearchController(filterSearchInteractor);
        filterSearchView.setFilterSearchController(filterSearchController);

        loggedInView.setFilterSearchNavigation(viewManagerModel, filterSearchView.getViewName());
        filterSearchView.setBackNavigation(viewManagerModel, loggedInView.getViewName());
        return this;
    }
    public AppBuilder addStockSearchUseCase() {
        stockSearchViewModel = new StockSearchViewModel();
        StockSearchOutputBoundary outputBoundary = new StockSearchPresenter(stockSearchViewModel);
        StockSearchInputBoundary interactor =
                new StockSearchInteractor(stockSearchDataAccessObject, outputBoundary);

        stockSearchController = new StockSearchController(interactor, stockSearchViewModel);
        loggedInView.setStockSearchController(stockSearchController);
        loggedInView.setStockSearchViewModel(stockSearchViewModel);
        return this;
    }

    public AppBuilder addNewsUsecase() {
        // Logged-in page: News button → News view
        loggedInView.setNewsNavigation(viewManagerModel, newsView.getViewName());

        // News page: Back button → Logged-in view
        newsView.setBackNavigation(viewManagerModel, loggedInView.getViewName());

        return this;
    }
    public AppBuilder addEarningsHistoryUseCase() {
        // Logged-in page: History button → Earnings history view
        loggedInView.setHistoryNavigation(
                viewManagerModel, earningsHistoryView.getViewName());

        // Earnings history page: Back button → Logged-in view
        earningsHistoryView.setBackNavigation(
                viewManagerModel, loggedInView.getViewName());

        return this;
    }

    public AppBuilder addAccount() {
        accountViewModel = new AccountViewModel();
        WatchlistOutputBoundary watchlistPresenter =
                new AccountPresenter(accountViewModel);
        WatchlistInputBoundary watchlistInteractor =
                new WatchlistInteractor(watchlistDataAccessObject, watchlistPresenter);
        RemoveWatchlistInputBoundary removeInteractor =
                new RemoveWatchlistInteractor(watchlistDataAccessObject, watchlistPresenter);
        AccountController accountController = new AccountController(watchlistInteractor, removeInteractor);

        accountView = new AccountView(accountViewModel);
        accountView.setController(accountController);
        cardPanel.add(accountView, accountView.getViewName());
        loggedInView.setAccountController(accountController);

        // Logged in page: Account button → Account view
        loggedInView.setAccountNavigation(
                viewManagerModel, accountView.getViewName());
        viewManagerModel.addPropertyChangeListener(evt -> {
            if (viewManagerModel.getState().equals(accountView.getViewName())) {
                String username = userDataAccessObject.getCurrentUsername();
                accountView.loadAccount(username);
            }
        });

        // Account page: Back button → Logged-in view
        accountView.setBackNavigation(
                viewManagerModel, loggedInView.getViewName());

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

    public AppBuilder addMarketStatusUseCase() {
        marketStatusViewModel = new MarketStatusViewModel();
        MarketStatusOutputBoundary msPresenter = new MarketStatusPresenter(marketStatusViewModel);
        MarketStatusDataAccessInterface msDao = marketStatusDataAccessObject;
        MarketStatusInputBoundary msInteractor = new MarketStatusInteractor(msDao, msPresenter);
        marketStatusController = new MarketStatusController(msInteractor);
        loggedInView.setMarketStatusViewModel(marketStatusViewModel);
        marketStatusController.updateStatus();
        return this;
    }

    public AppBuilder addRealtimeTradeUseCase() {
        String tradeViewName = tradeView.getViewName();
        loggedInView.setRealtimeTradeNavigation(viewManagerModel, tradeViewName);
        tradeView.setBackNavigation(viewManagerModel, loggedInView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Stock Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}