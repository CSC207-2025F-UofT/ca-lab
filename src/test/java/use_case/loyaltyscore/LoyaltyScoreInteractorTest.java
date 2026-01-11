package use_case.loyaltyscore;

import data_access.LoyaltyScoreDataAccessObject;
import data_access.SpotifyDataAccessObject;
import entity.ArtistLoyaltyScore;
import entity.SpotifyUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.loyalty_score.*;


import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class LoyaltyScoreInteractorTest {

    @Test
    void successTest() {

        final String DATE_1 = "2025-04-12";
        final String DATE_2 = "2025-04-13";
        final int SCORE_DATE_1 = 90;
        final int SCORE_DATE_2 = 590;
        final String ARTIST_1 = "Artist1";
        final SpotifyUser SPOTIFY_USER = new SpotifyUser("kellyh", "a",
                "t", "kh1203");

        final String USERID = SPOTIFY_USER.getSpotifyUserId();


        LoyaltyScoreInputData INPUT_DATA = new LoyaltyScoreInputData(SPOTIFY_USER, ARTIST_1);



        // Mock the SpotifyDataAccessObject
        SpotifyDataAccessObject mockSpotifyDAO = mock(SpotifyDataAccessObject.class);

        ArtistLoyaltyScore als1 = new ArtistLoyaltyScore("Artist1", "id1",
                2, 10, true);

        int ALS_current = als1.getLoyaltyScore();

        ArtistLoyaltyScore als2 = new ArtistLoyaltyScore("Artist2", "id2",
                3, 20, true);


        // Mock the return value of getArtistLoyaltyScores
        List<ArtistLoyaltyScore> mockLoyaltyScores = List.of(als1, als2);

        when(mockSpotifyDAO.getArtistLoyaltyScores(SPOTIFY_USER)).thenReturn(mockLoyaltyScores);

        // Mock the LoyaltyScoreDataAccessObject (this is for saving scores)
        LoyaltyScoreDataAccessObject dataAccessObject = new LoyaltyScoreDataAccessObject();



        // This creates a successPresenter that tests whether the test case is as we expect.
        LoyaltyScoreOutputBoundary successPresenter = new LoyaltyScoreOutputBoundary() {
            @Override
            public void prepareView(LoyaltyScoreOutputData outputData) {
                assertEquals(SCORE_DATE_1, outputData.getScores().get(DATE_1));
                assertEquals(SCORE_DATE_2, outputData.getScores().get(DATE_2));

                // ONLY expect 3; since one call is for Artist2
                assertEquals(3, outputData.getScores().size());
                assertEquals(ALS_current, outputData.getScores().get(LocalDate.now().toString()));
            }

            @Override
            public void returnPreviousView(){}
        };

        // Create the interactor with the mock Spotify DAO and the mock data access object
        LoyaltyScoreInputBoundary interactor = new LoyaltyScoreInteractor(dataAccessObject, successPresenter, mockSpotifyDAO);

        dataAccessObject.saveLoyalty(USERID, DATE_1, ARTIST_1, SCORE_DATE_1);
        dataAccessObject.saveLoyalty(USERID, DATE_2, ARTIST_1, SCORE_DATE_2);

        // Execute the interactor (this will trigger the mock SpotifyDAO)
        interactor.execute(INPUT_DATA);

        // Verify interactions with the mock objects
        verify(mockSpotifyDAO).getArtistLoyaltyScores(SPOTIFY_USER);
    }
    @BeforeEach
    public void setup() {

        // Ensure the file is clean before each test
        final String testFilePath = "loyaltyscore_kh1203.json";
        File testFile = new File(testFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}
