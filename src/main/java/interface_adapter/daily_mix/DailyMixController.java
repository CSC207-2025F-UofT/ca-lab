package interface_adapter.daily_mix;

import entity.SpotifyUser;
import use_case.daily_mix.DailyMixInputBoundary;
import use_case.daily_mix.DailyMixInputData;

public class DailyMixController {

    private final DailyMixInputBoundary dailyMixInteractor;

    public DailyMixController(DailyMixInputBoundary dailyMixInteractor) {
        this.dailyMixInteractor = dailyMixInteractor;
    }

    public void execute(SpotifyUser spotifyUser, int mixSize) {
        DailyMixInputData inputData = new DailyMixInputData(spotifyUser, mixSize);
        dailyMixInteractor.execute(inputData);
    }
}

