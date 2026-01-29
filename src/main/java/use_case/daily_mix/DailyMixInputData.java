package use_case.daily_mix;

import entity.SpotifyUser;

public class DailyMixInputData {
    private final SpotifyUser spotifyUser;
    private final int mixSize;

    public DailyMixInputData(SpotifyUser spotifyUser, int mixSize) {
        this.spotifyUser = spotifyUser;
        this.mixSize = mixSize;
    }

    public SpotifyUser getSpotifyUser() {
        return spotifyUser;
    }

    public int getMixSize() {
        return mixSize;
    }
}

