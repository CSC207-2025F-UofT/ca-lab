package interface_adapter.loyalty_score;

import java.util.List;
import java.util.Map;

public class LoyaltyState {
    private Map<String, Integer> loyaltyScores;  // List to hold the loyalty scores for different artists
    private String currentArtist;  // Current artist that the user is viewing
    private boolean isLoading;  // Indicates whether data is being loaded
    private boolean isError;  // Indicates whether there was an error fetching the data

    // Getters and Setters

    public Map<String, Integer> getLoyaltyScores() {
        return this.loyaltyScores;
    }

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
