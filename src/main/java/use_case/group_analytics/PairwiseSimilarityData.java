package use_case.group_analytics;

public class PairwiseSimilarityData {

    private final String userA;
    private final String userB;
    private final double similarity; // 0–1

    public PairwiseSimilarityData(String userA, String userB, double similarity) {
        this.userA = userA;
        this.userB = userB;
        this.similarity = similarity;
    }

    public String getUserA() {
        return userA;
    }

    public String getUserB() {
        return userB;
    }

    public double getSimilarity() {
        return similarity;
    }
}
