package use_case.choose_fav_pokemon;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;
import view.GameDashboard;

/**
 * The interactor for the Choose Favourite Pokémon use case.
 */
public class ChooseFavPokemonInteractor implements ChooseFavPokemonInputBoundary {

    private final FileUserDataAccessObject userDataAccessObject;
    private final ChooseFavPokemonOutputBoundary userPresenter;
    private final UserFactory userFactory;
    private GameDashboard dashboard;

    public ChooseFavPokemonInteractor(FileUserDataAccessObject userDataAccessObject,
                                 ChooseFavPokemonOutputBoundary userPresenter,
                                 UserFactory userFactory, GameDashboard dashboard) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = userPresenter;
        this.userFactory = userFactory;
        this.dashboard = dashboard;
    }

    /**
     * Executes the Choose Favourite Pokémon use case.
     */
    @Override
    public void execute(ChooseFavPokemonInputData chooseFavPokemonInputData) {
        User u = ((FileUserDataAccessObject)userDataAccessObject).get(dashboard.getCurrentUser());
        final User user = userFactory.create(u.getName(),
                u.getPassword(),
                u.getScore(),
                u.getBio(),
                chooseFavPokemonInputData.getFav_pokemon());

        userDataAccessObject.editProfile(user);

        final ChooseFavPokemonOutputData chooseFavPokemonOutputData =
                new ChooseFavPokemonOutputData(user.getName(), user.getBio(), user.getFavPokemon());
        userPresenter.prepareSuccessView(chooseFavPokemonOutputData);
    }
}
