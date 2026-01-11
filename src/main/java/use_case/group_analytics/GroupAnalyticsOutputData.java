package use_case.group_analytics;

import entity.GroupVerdict;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GroupAnalyticsOutputData {

    private final List<PairwiseSimilarityData> pairwise;
    private final double averageSimilarity;
    private final Map<String, Double> vibeScores;
    private final GroupVerdict verdict;

    public GroupAnalyticsOutputData(List<PairwiseSimilarityData> pairwise,
                                    double averageSimilarity,
                                    Map<String, Double> vibeScores,
                                    GroupVerdict verdict) {
        this.pairwise = Collections.unmodifiableList(pairwise);
        this.averageSimilarity = averageSimilarity;
        this.vibeScores = Collections.unmodifiableMap(vibeScores);
        this.verdict = verdict;
    }

    public List<PairwiseSimilarityData> getPairwise() {
        return pairwise;
    }

    public double getAverageSimilarity() {
        return averageSimilarity;
    }

    public Map<String, Double> getVibeScores() {
        return vibeScores;
    }

    public GroupVerdict getVerdict() {
        return verdict;
    }
}
