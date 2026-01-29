package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple entity representing a playlist. A playlist has a playlist_name, list_songs,
 * and date_created.
 * Recall that each song is a string link to the spotify song.
 */
public class Playlist {

    private String playlist_name;
    private final List<String> list_songs;
    private final String date_created;

    /**
     * Creates a new playlist given these parameters:
     * @param playlist_name the playlist's name
     * @param date the date the playlist was created
     */
    public Playlist(String playlist_name, String date ) {
        this.playlist_name = playlist_name;
        this.date_created = date;
        this.list_songs = new ArrayList<String>();
    }

    public String getName() {
        return this.playlist_name;
    }
    public String getDate() { return this.date_created; }
    public List<String> getSongs() {return this.list_songs;}
    public void addSong(String song) {this.list_songs.add(song);}
    public void changeName(String new_name) {this.playlist_name = new_name;}

}
