package interface_adapter.spotify_auth;

import interface_adapter.ViewModel;

public class SpotifyAuthViewModel extends ViewModel<SpotifyAuthState> {
    public SpotifyAuthViewModel() {
        super("spotify auth");
        setState(new SpotifyAuthState());
    }
}
