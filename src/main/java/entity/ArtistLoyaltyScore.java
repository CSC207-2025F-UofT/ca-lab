package entity;

public class ArtistLoyaltyScore {
    private final String artistName;
    private final String artistId;
    private final int savedTracks;
    private final int savedAlbums;
    private final boolean inRecentlyPlayed;
    private final int loyaltyScore;

    public ArtistLoyaltyScore(String artistName, String artistId, int savedTracks,
                              int savedAlbums, boolean inRecentlyPlayed) {
        this.artistName = artistName;
        this.artistId = artistId;
        this.savedTracks = savedTracks;
        this.savedAlbums = savedAlbums;
        this.inRecentlyPlayed = inRecentlyPlayed;
        this.loyaltyScore = calculateLoyaltyScore();
    }

    private int calculateLoyaltyScore() {
        // Loyalty score formula:
        // - Each saved track: 10 points
        // - Each saved album: 50 points
        // - In recently played: 100 points bonus
        int score = (savedTracks * 10) + (savedAlbums * 50);
        if (inRecentlyPlayed) {
            score += 100;
        }
        return score;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public int getSavedTracks() {
        return savedTracks;
    }

    public int getSavedAlbums() {
        return savedAlbums;
    }

    public boolean isInRecentlyPlayed() {
        return inRecentlyPlayed;
    }

    public int getLoyaltyScore() {
        return loyaltyScore;
    }

    @Override
    public String toString() {
        return String.format("%s - Score: %.0f (Tracks: %d, Albums: %d, Recent: %s)",
                artistName, loyaltyScore, savedTracks, savedAlbums,
                inRecentlyPlayed ? "Yes" : "No");
    }
}