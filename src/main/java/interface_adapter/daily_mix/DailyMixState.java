package interface_adapter.daily_mix;


import java.util.ArrayList;
import java.util.List;

public class DailyMixState {

    private List<String> tracks = new ArrayList<>();
    private String error;

    public List<String> getTracks() {
        return tracks;
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}

