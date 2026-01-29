package use_case.loyalty_score;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data_access.SpotifyDataAccessObject;
import entity.ArtistLoyaltyScore;

public class LoyaltyScoreInteractor implements LoyaltyScoreInputBoundary {

    private final LoyaltyScoreDataAccessInterface loyaltyDataAccessObject;
    private final LoyaltyScoreOutputBoundary loyaltyPresenter;
    private final SpotifyDataAccessObject spotifyDAO;

    public LoyaltyScoreInteractor(LoyaltyScoreDataAccessInterface loyaltyScoreDataAccessInterface,
                                  LoyaltyScoreOutputBoundary loyaltyScoreOutputBoundary,
                                  SpotifyDataAccessObject spotifyDAO) {
        this.loyaltyDataAccessObject = loyaltyScoreDataAccessInterface;
        this.loyaltyPresenter = loyaltyScoreOutputBoundary;
        this.spotifyDAO = spotifyDAO;

    }

    @Override
    public void execute(LoyaltyScoreInputData loyaltyScoreInputData) {

        final String userid = loyaltyScoreInputData.getSpotifyUser().getSpotifyUserId();
        final String chosen_artist = loyaltyScoreInputData.getArtist_name();

        // update loyalty scores for current visit
        update_loyalty_scores(loyaltyScoreInputData);

        // user chooses a specific artist to view loyalty scores for; so
        final Map<String, Integer> loyalty_scores = loyaltyDataAccessObject.getLoyaltyArtist(userid, chosen_artist);
        final List<String> dates = loyaltyDataAccessObject.getDates(userid);

        // output data is loyalty scores
        final LoyaltyScoreOutputData outputData = new LoyaltyScoreOutputData(loyalty_scores, dates, chosen_artist);

        loyaltyPresenter.prepareView(outputData);
    }

    private void update_loyalty_scores(LoyaltyScoreInputData loyaltyScoreInputData) {
        String currentDate = LocalDate.now().toString();
        String userid = loyaltyScoreInputData.getSpotifyUser().getSpotifyUserId();
        List<String> dates =  loyaltyDataAccessObject.getDates(userid);

        List<ArtistLoyaltyScore> loyaltyScores = spotifyDAO.getArtistLoyaltyScores(
                                                loyaltyScoreInputData.getSpotifyUser());

        // return early if today's loyalty has already been calculated.
        if (dates.contains(currentDate)) {
            return;
        }

        // get loyalty scores for today and save them
        for  (ArtistLoyaltyScore loyaltyScore : loyaltyScores) {
            String artist_name = loyaltyScore.getArtistName();
            int loyalty = loyaltyScore.getLoyaltyScore();
            loyaltyDataAccessObject.saveLoyalty(userid, currentDate, artist_name, loyalty);
        }
    }
}
