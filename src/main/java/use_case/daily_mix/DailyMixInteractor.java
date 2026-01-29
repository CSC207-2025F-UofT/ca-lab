package use_case.daily_mix;

import data_access.SpotifyDataAccessObject;
import entity.SpotifyUser;

import java.util.List;

public class DailyMixInteractor implements DailyMixInputBoundary {

    private final SpotifyDataAccessObject spotifyDAO;
    private final DailyMixOutputBoundary dailyMixPresenter;

    public DailyMixInteractor(SpotifyDataAccessObject spotifyDAO,
                              DailyMixOutputBoundary dailyMixPresenter) {
        this.spotifyDAO = spotifyDAO;
        this.dailyMixPresenter = dailyMixPresenter;
    }

    @Override
    public void execute(DailyMixInputData inputData) {
        try {
            SpotifyUser user = inputData.getSpotifyUser();
            int size = inputData.getMixSize();

            List<String> mix = spotifyDAO.generateDailyMix(user, size);

            DailyMixOutputData outputData = new DailyMixOutputData(mix);
            dailyMixPresenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            dailyMixPresenter.prepareFailView("Failed to generate Daily Mix: " + e.getMessage());
        }
    }
}

