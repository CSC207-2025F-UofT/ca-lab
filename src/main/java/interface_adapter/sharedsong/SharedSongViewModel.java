package interface_adapter.sharedsong;

import interface_adapter.ViewModel;

import java.util.HashMap;

public class SharedSongViewModel extends ViewModel<SharedSongState> {

    public SharedSongViewModel() {
        super("shared song");
        SharedSongState initialState = new SharedSongState();
        initialState.setUsernameToShared(new HashMap<>());
        setState(initialState);
    }

    @Override
    public SharedSongState getState() {
        return super.getState();
    }
}