package interface_adapter.loyalty_score;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoyaltyState {
    private ArrayList<String> dates; // List of dates
    private Map<String, Integer> loyaltyScores;  // Map to hold the loyalty scores for different artists
    private String currentArtist;  // Current artist that the user is viewing
    private boolean isLoading;  // Indicates whether data is being loaded
    private boolean isError;  // Indicates whether there was an error fetching the data

    // Getters and Setters

    public Map<String, Integer> getLoyaltyScores() {
        return this.loyaltyScores;
    }

    public ArrayList<String> getDates() { return this.dates; }

    public void setDates(ArrayList<String> dates ) { this.dates = dates; }

    public void setLoyaltyScores(Map<String, Integer> loyaltyScores) {
        this.loyaltyScores = loyaltyScores;
    }

    public String getCurrentArtist() {
        return this.currentArtist;
    }

    public void setCurrentArtist(String currentArtist) {
        this.currentArtist = currentArtist;
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }

    public boolean isError() {
        return this.isError;
    }

    public void setError(boolean error) {
        this.isError = error;
    }
}
