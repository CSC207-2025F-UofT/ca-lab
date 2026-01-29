package use_case.loyalty_score;

import entity.TopItem;

import java.util.List;
import java.util.Map;

/**
 * DAO interface for the LoyaltyScore Use Case
 */
public interface LoyaltyScoreDataAccessInterface {

    /**
     * @return A map where the key is the name of object, integer is
     * saved loyalty_score
     */
    Map<String, Integer> getLoyaltyDate(String userid, String date);

    /**
     * @return Whether the artist exists in that date's saved loyalty scores
     */

    boolean loyaltyScoreExists(String userid, String date, String artist_name);

    /**
     * @param userid who to save to
     * @param date the date of saving
     * @param artist_name name of the artist
     * @param score the score.
     * Saves loyalty score
     */
    void saveLoyalty(String userid, String date, String artist_name, int score);

    /**
     * @return A map where the key is the date, integer is
     * saved loyalty_score for that artist
     */

    Map<String, Integer> getLoyaltyArtist(String userid, String artist_name);


    /**
     * @return all previous calculated dates
     */
    List<String> getDates(String userid);
}
