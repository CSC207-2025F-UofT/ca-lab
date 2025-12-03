package interface_adapter.user_profile;

import entity.User;
import interface_adapter.ViewModel;

public class UserProfileViewModel extends ViewModel<UserProfileState> {

    public UserProfileViewModel(User u) {
        super("User Profile");
        setState(new UserProfileState(u));
    }

    public UserProfileViewModel() {
        super("User Profile");
        setState(new UserProfileState());
    }
}

