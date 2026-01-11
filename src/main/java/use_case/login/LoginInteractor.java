package use_case.login;

import entity.SpotifyUser;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        System.out.println("DEBUG: Login attempt - username: " + username);
        System.out.println("DEBUG: Login attempt - password entered: " + password);

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            // dummy password
            final String pwd = "";
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {

                final SpotifyUser user = userDataAccessObject.get(loginInputData.getUsername());

                userDataAccessObject.setCurrentUsername(username);

                final LoginOutputData loginOutputData = new LoginOutputData(user.getUsername());
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
}
