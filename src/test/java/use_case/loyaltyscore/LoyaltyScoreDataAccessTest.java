package use_case.loyaltyscore;

import data_access.LoyaltyScoreDataAccessObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LoyaltyScoreDataAccessTest {

    private LoyaltyScoreDataAccessObject dataAccess;
    private final String userId = "520";
    private final String testFilePath = "loyaltyscore_520.json";
    private final String DATE = "2023-11-23";
    private final String DATE_2 = "2024-11-25";
    private final int SCORE_1 = 85;
    private final int SCORE_2 = 150;
    private final String ARTIST_1 = "Artist1";
    private final String ARTIST_2 = "Artist2";

    @BeforeEach
    public void setup() {
        // Set up the data access object before each test
        dataAccess = new LoyaltyScoreDataAccessObject();

        // Ensure the file is clean before each test
        File testFile = new File(testFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testSaveLoyaltyFirstTime() {

        // Act: Save the loyalty score for the first time
        dataAccess.saveLoyalty(userId, DATE, ARTIST_1, SCORE_1);

        // Assert: Check if the file now exists and the score is saved
        File testFile = new File(testFilePath);
        assertTrue(testFile.exists(), "Test file should be created.");

        // Check that the loyalty score is correct
        Map<String, Integer> loyaltyScores = dataAccess.getLoyaltyDate(userId, DATE);
        assertEquals(1, loyaltyScores.size(), "Should have 1 artist score.");
        assertEquals(SCORE_1, loyaltyScores.get(ARTIST_1));
    }

    @Test
    public void testGetLoyaltyDate() {
        // Arrange: Save loyalty scores for a given date
        dataAccess.saveLoyalty(userId, DATE, ARTIST_1, SCORE_1);
        dataAccess.saveLoyalty(userId, DATE, ARTIST_2, SCORE_2);

        // Act: Retrieve the loyalty scores for that date
        Map<String, Integer> loyaltyScores = dataAccess.getLoyaltyDate(userId, DATE);

        // Assert: Check if the correct scores are returned
        assertEquals(2, loyaltyScores.size(), "Should have 2 artist scores.");
        assertEquals(SCORE_1, loyaltyScores.get(ARTIST_1));
        assertEquals(SCORE_2, loyaltyScores.get(ARTIST_2));
    }

    @Test
    public void testGetLoyaltyArtist() {
        // Arrange: Save loyalty scores for a given date
        dataAccess.saveLoyalty(userId, DATE, ARTIST_1, SCORE_1);
        dataAccess.saveLoyalty(userId, DATE_2, ARTIST_1, SCORE_2);

        // Act: Retrieve the loyalty scores for that date
        Map<String, Integer> loyaltyScores = dataAccess.getLoyaltyArtist(userId, ARTIST_1);

        // Assert: Check if the correct scores are returned
        assertEquals(2, loyaltyScores.size());
        assertEquals(SCORE_1, loyaltyScores.get(DATE));
        assertEquals(SCORE_2, loyaltyScores.get(DATE_2));
    }

    @Test
    public void testLoyaltyScoreExists() {
        // Arrange: Save a loyalty score for a specific artist
        dataAccess.saveLoyalty(userId, DATE, ARTIST_1, SCORE_1);

        // Act: Check if the loyalty score exists for that artist and date
        boolean exists = dataAccess.loyaltyScoreExists(userId, DATE, ARTIST_1);
        boolean notExists = dataAccess.loyaltyScoreExists(userId, DATE, ARTIST_2);

        // Assert: Verify that the score exists for Artist1, but not for Artist2
        assertTrue(exists, "Loyalty score should exist for Artist1.");
        assertFalse(notExists, "Loyalty score should not exist for Artist2.");
    }

    @Test
    public void testSaveLoyaltyUpdateScore() {
        // Arrange: Save an initial loyalty score for an artist
        dataAccess.saveLoyalty(userId, DATE, ARTIST_1, SCORE_1);

        // Act: Save a new score for the same artist
        dataAccess.saveLoyalty(userId, DATE, ARTIST_1, SCORE_2);

        // Assert: Check if the updated score is saved correctly
        Map<String, Integer> loyaltyScores = dataAccess.getLoyaltyDate(userId, DATE);
        assertEquals(1, loyaltyScores.size(), "Should have 1 artist score.");
        assertEquals(SCORE_2, loyaltyScores.get(ARTIST_1), "The updated score for Artist1 should be 90.");
    }

    @Test
    public void testFileDoesNotExist() {
        // Arrange: Ensure the file doesn't exist
        File testFile = new File(testFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }

        // Act: Try to get loyalty data when the file doesn't exist
        Map<String, Integer> loyaltyScores = dataAccess.getLoyaltyDate(userId, DATE);

        // Assert: The result should be an empty map
        assertTrue(loyaltyScores.isEmpty(), "There should be no loyalty scores when the file does not exist.");
    }

    @Test
    public void testEmptyFile() throws IOException {
        // Arrange: Manually create an empty JSON file
        File testFile = new File(testFilePath);
        testFile.createNewFile();

        // Act: Try to get loyalty data from an empty file
        Map<String, Integer> loyaltyScores = dataAccess.getLoyaltyDate(userId, DATE);

        // Assert: The result should be an empty map since the file is empty
        assertTrue(loyaltyScores.isEmpty(), "There should be no loyalty scores in the empty file.");
    }
}
