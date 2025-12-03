package interface_adapter.user_profile;

import interface_adapter.ViewManagerModel;
import use_case.user_profile.UserProfileOutputBoundary;
import use_case.user_profile.UserProfileOutputData;

public class UserProfilePresenter implements UserProfileOutputBoundary {
    private final UserProfileViewModel userProfileViewModel;
    private final ViewManagerModel viewManagerModel;

    public UserProfilePresenter(UserProfileViewModel userProfileViewModel,
                                ViewManagerModel viewManagerModel) {
        this.userProfileViewModel = userProfileViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(UserProfileOutputData outputData) {
        UserProfileState state = userProfileViewModel.getState();
        state.setUsername(outputData.getUsername());
        state.setName(outputData.getName());
        state.setProfilePhotoPath(outputData.getProfilePhotoPath());
        state.setBannerPath(outputData.getBannerPath());
        state.setBio(outputData.getBio());
        state.setFav_pokemon(outputData.getFavPokemon());
        state.setProfileCompletionPercentage(outputData.getProfileCompletionPercentage());
        state.setBioCharacterCount(outputData.getBioCharacterCount());
        state.setProfileError(null);
        userProfileViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        userProfileViewModel.getState().setProfileError(error);
        userProfileViewModel.firePropertyChange();
    }
}

