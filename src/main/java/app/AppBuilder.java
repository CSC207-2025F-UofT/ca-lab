package app;

import data_access.LoyaltyScoreDataAccessObject;
import data_access.SpotifyDataAccessObject;
import data_access.TopItemDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.InGroupViewModel;
import interface_adapter.create_group.NoGroupViewModel;
import interface_adapter.get_topItems.GetTopItemsController;
import interface_adapter.get_topItems.GetTopItemsPresenter;
import interface_adapter.get_topItems.GetTopItemsViewModel;
import interface_adapter.group_analytics.GroupAnalyticsController;
import interface_adapter.group_analytics.GroupAnalyticsPresenter;
import interface_adapter.group_analytics.GroupAnalyticsViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.loyalty_score.LoyaltyController;
import interface_adapter.loyalty_score.LoyaltyPresenter;
import interface_adapter.loyalty_score.LoyaltyViewModel;
import interface_adapter.sharedsong.SharedSongController;
import interface_adapter.sharedsong.SharedSongPresenter;
import interface_adapter.sharedsong.SharedSongViewModel;
import interface_adapter.spotify_auth.SpotifyAuthController;
import interface_adapter.spotify_auth.SpotifyAuthPresenter;
import interface_adapter.spotify_auth.SpotifyAuthViewModel;
import interface_adapter.daily_mix.DailyMixViewModel;
import interface_adapter.daily_mix.DailyMixController;
import interface_adapter.daily_mix.DailyMixPresenter;
import use_case.get_topItems.GetTopItemsInputBoundary;
import use_case.get_topItems.GetTopItemsInteractor;
import use_case.get_topItems.GetTopItemsOutputBoundary;
import use_case.group_analytics.GroupAnalyticsInteractor;
import use_case.group_analytics.GroupAnalyticsInputBoundary;
import use_case.group_analytics.GroupAnalyticsOutputBoundary;
import use_case.loyalty_score.LoyaltyScoreInputBoundary;
import use_case.loyalty_score.LoyaltyScoreInteractor;
import use_case.loyalty_score.LoyaltyScoreOutputBoundary;
import use_case.sharedsong.SharedSongInputBoundary;
import use_case.sharedsong.SharedSongInteractor;
import use_case.sharedsong.SharedSongOutputBoundary;
import use_case.spotify_auth.SpotifyAuthInputBoundary;
import use_case.spotify_auth.SpotifyAuthInteractor;
import use_case.spotify_auth.SpotifyAuthOutputBoundary;
import use_case.daily_mix.DailyMixInputBoundary;
import use_case.daily_mix.DailyMixInteractor;
import use_case.daily_mix.DailyMixOutputBoundary;
import view.*;
import view.group_analytics.GroupAnalyticsView;

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
    private SpotifyAuthView spotifyAuthView;
    private SpotifyAuthViewModel spotifyAuthViewModel;

    private InGroupView inGroupView;
    private InGroupViewModel inGroupViewModel;
    private NoGroupView noGroupView;
    private NoGroupViewModel noGroupViewModel;
    private SharedSongView sharedSongView;
    private SharedSongViewModel sharedSongViewModel;
    private GroupAnalyticsView groupAnalyticsView;
    private GroupAnalyticsViewModel groupAnalyticsViewModel;

    private DailyMixViewModel dailyMixViewModel;
    private GetTopItemsViewModel getTopItemsViewModel;
    private LoyaltyViewModel loyaltyViewModel;
    private LoyaltyScoreView loyaltyView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addViewModels() {
        loggedInViewModel = new LoggedInViewModel();
        spotifyAuthViewModel = new SpotifyAuthViewModel();
        inGroupViewModel = new InGroupViewModel();
        noGroupViewModel = new NoGroupViewModel();
        sharedSongViewModel = new SharedSongViewModel();
        groupAnalyticsViewModel = new GroupAnalyticsViewModel();
        dailyMixViewModel = new DailyMixViewModel();
        getTopItemsViewModel = new GetTopItemsViewModel();
        loyaltyViewModel = new LoyaltyViewModel();
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInView = new LoggedInView(loggedInViewModel,
                viewManagerModel,
                spotifyAuthViewModel,
                dailyMixViewModel,
                getTopItemsViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addSpotifyAuthView() {
        spotifyAuthView = new SpotifyAuthView(spotifyAuthViewModel, loggedInViewModel);  // Pass loggedInViewModel
        spotifyAuthView.setViewManagerModel(viewManagerModel);
        cardPanel.add(spotifyAuthView, spotifyAuthView.getViewName());
        return this;
    }

    public AppBuilder addInGroupView() {
        inGroupView = new InGroupView(viewManagerModel, inGroupViewModel, sharedSongViewModel, groupAnalyticsViewModel);
        cardPanel.add(inGroupView, inGroupView.getViewName());
        return this;
    }

    public AppBuilder addNoGroupView() {
        noGroupView = new NoGroupView(noGroupViewModel, viewManagerModel);
        cardPanel.add(noGroupView, noGroupView.getViewName());
        return this;
    }
    public AppBuilder addCreateGroupUseCase() {
        // Presenter
        use_case.create_group.CreateGroupOutputBoundary outputBoundary =
                new interface_adapter.create_group.CreateGroupPresenter(
                        inGroupViewModel,
                        noGroupViewModel,
                        viewManagerModel);

        // Interactor
        use_case.create_group.CreateGroupInputBoundary interactor =
                new use_case.create_group.CreateGroupInteractor(
                        new data_access.GroupDataAccessObject());

        // Controller gets BOTH interactor and presenter
        interface_adapter.create_group.CreateGroupController controller =
                new interface_adapter.create_group.CreateGroupController(interactor, outputBoundary);

        // Give controller to the LoggedInView (popup)
        loggedInView.setCreateGroupController(controller);

        // Also give controller to NoGroupView (join/create group screen)
        noGroupView.setCreateGroupController(controller);

        return this;
    }

    public AppBuilder addSharedSongView() {
        sharedSongView = new SharedSongView(inGroupViewModel, sharedSongViewModel, viewManagerModel);
        cardPanel.add(sharedSongView, sharedSongView.getViewName());
        return this;
    }


    public AppBuilder addGroupAnalyticsView() {
        groupAnalyticsView = new GroupAnalyticsView(groupAnalyticsViewModel,
                viewManagerModel);
        cardPanel.add(groupAnalyticsView, groupAnalyticsView.getViewName());
        return this;
    }

    public AppBuilder addLoyaltyView() {
        loyaltyView = new LoyaltyScoreView(loyaltyViewModel, viewManagerModel);
        cardPanel.add(loyaltyView, loyaltyView.getViewName());
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

    public AppBuilder addLeaveGroupUseCase() {
//        TODO uncomment once GroupDAO is done
//        final LeaveGroupOutputBoundary leaveGroupPresenter =
//                new LeaveGroupPresenter(viewManagerModel, inGroupViewModel, noGroupViewModel);
//        final LeaveGroupInputBoundary leaveGroupInteractor =
//                new LeaveGroupInteractor(new GroupDataAccessObject(), leaveGroupPresenter);
//        LeaveGroupController leaveGroupController = new LeaveGroupController(leaveGroupInteractor);
//        inGroupView.setLeaveGroupController(leaveGroupController);
        return this;
    }

    public AppBuilder addSharedSongUseCase() {
        final SharedSongOutputBoundary sharedSongPresenter =
                new SharedSongPresenter(sharedSongViewModel, viewManagerModel);
        final SharedSongInputBoundary sharedSongInteractor =
                new SharedSongInteractor(sharedSongPresenter, new SpotifyDataAccessObject());
        final SharedSongController sharedSongController = new SharedSongController(sharedSongInteractor);
        inGroupView.setSharedSongController(sharedSongController);
        return this;
    }

    public AppBuilder addGroupAnalyticsUseCase() {
        final GroupAnalyticsOutputBoundary outputBoundary =
                new GroupAnalyticsPresenter(groupAnalyticsViewModel);

        final GroupAnalyticsInputBoundary interactor =
                new GroupAnalyticsInteractor(outputBoundary);

        GroupAnalyticsController controller =
                new GroupAnalyticsController(interactor);

        // The button that triggers analytics lives inside InGroupView
        inGroupView.setGroupAnalyticsController(controller);

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

    public AppBuilder addGetTopItemsUseCase() {

        final GetTopItemsOutputBoundary getTopItemsOutputBoundary =
                new GetTopItemsPresenter(getTopItemsViewModel);

        final GetTopItemsInputBoundary getTopItemsInteractor =
                new GetTopItemsInteractor(getTopItemsOutputBoundary, new TopItemDataAccessObject());

        GetTopItemsController getTopItemsController = new GetTopItemsController(getTopItemsInteractor);
        loggedInView.setGetTopItemsController(getTopItemsController);

        return this;
    }

    public AppBuilder addLoyaltyUseCase() {
        final LoyaltyScoreOutputBoundary loyaltyScoreOutputBoundary = new LoyaltyPresenter(viewManagerModel, loyaltyViewModel);

        final LoyaltyScoreInputBoundary loyaltyScoreInteractor = new LoyaltyScoreInteractor(new LoyaltyScoreDataAccessObject(),
                                                    loyaltyScoreOutputBoundary, new SpotifyDataAccessObject());

        LoyaltyController loyaltyController = new LoyaltyController(loyaltyScoreInteractor);
        loyaltyView.setLoyaltyController(loyaltyController);
        loggedInView.setLoyaltyLookupController(loyaltyController);

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Better Wrapped - Spotify Analysis");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(loggedInView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
