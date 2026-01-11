package interface_adapter.group_analytics;

import entity.GroupVerdict;
import use_case.group_analytics.PairwiseSimilarityData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupAnalyticsState {

    private List<PairwiseSimilarityData> pairwise = new ArrayList<>();
    private double averageSimilarity;
    private Map<String, Double> vibeScores;
    private GroupVerdict verdict;
    private String errorMessage;

    public List<PairwiseSimilarityData> getPairwise() {
        return pairwise;
    }

    public void setPairwise(List<PairwiseSimilarityData> pairwise) {
        this.pairwise = pairwise;
    }

    public double getAverageSimilarity() {
        return averageSimilarity;
    }

    public void setAverageSimilarity(double averageSimilarity) {
        this.averageSimilarity = averageSimilarity;
    }

    public Map<String, Double> getVibeScores() {
        return vibeScores;
    }

    public void setVibeScores(Map<String, Double> vibeScores) {
        this.vibeScores = vibeScores;
    }

    public GroupVerdict getVerdict() {
        return verdict;
    }

    public void setVerdict(GroupVerdict verdict) {
        this.verdict = verdict;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
