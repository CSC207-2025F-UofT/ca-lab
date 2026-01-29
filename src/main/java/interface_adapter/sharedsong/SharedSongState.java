package interface_adapter.sharedsong;

import entity.SpotifyUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharedSongState {

    // input data that need to be passed to use case controllers
    private SpotifyUser spotifyUser;
    private List<SpotifyUser> groupUsers;
    private Map<String, String> usernameToShared = null;
    private String errorMessage = "";
    //    private String songName = "";
    //    private String artist = "";

    public SpotifyUser getSpotifyUser() { return spotifyUser; }

    public void setSpotifyUser(SpotifyUser spotifyUser) { this.spotifyUser = spotifyUser; }

    public List<SpotifyUser> getGroupUsers() { return this.groupUsers; }

    public void setGroupUsers(List<SpotifyUser> groupUsers) {
        this.groupUsers = new ArrayList<SpotifyUser>(groupUsers);
    }

    public void setUsernameToShared(Map<String, String> sharedSongOutputData) {
        this.usernameToShared = sharedSongOutputData;
    }

    public Map<String, String> getUsernameToShared() { return usernameToShared; }

    public String getErrorMessage() { return errorMessage; }

    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

//    public String getSongName() { return songName; }
//
//    public void setSongName(String songName) { this.songName = songName; }
//
//    public String getArtist() { return artist; }
//
//    public void setArtist(String artist) { this.artist = artist; }
}
