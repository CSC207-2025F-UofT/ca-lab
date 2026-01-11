package use_case.group_analytics;

import entity.GroupVerdict;
import entity.UserTasteProfile;

import java.util.*;

public class GroupAnalyticsInteractor implements GroupAnalyticsInputBoundary {

    private static final int MAX_GROUP_SIZE = 7;

    private final GroupAnalyticsOutputBoundary presenter;

    private static final Set<String> DANCE_GENRES = Set.of(
            "dance pop", "edm", "house", "electronic", "electropop",
            "k-pop", "kpop", "hip hop", "rap", "dance", "disco"
    );

    private static final Set<String> CRY_GENRES = Set.of(
            "sad", "indie", "indie pop", "indie folk", "acoustic",
            "folk", "r&b", "soul", "emo", "ballad"
    );

    private static final Set<String> ROADTRIP_GENRES = Set.of(
            "rock", "classic rock", "alt rock", "alternative rock",
            "indie rock", "pop rock", "country", "country road"
    );

    private static final Set<String> OLD_SOUL_GENRES = Set.of(
            "jazz", "blues", "classical", "swing", "soul", "motown",
            "oldies", "70s", "80s", "60s"
    );

    private static final Set<String> PODCASTY_GENRES = Set.of(
            "spoken word", "podcast", "audiobook", "chillhop",
            "lo-fi", "lofi", "ambient"
    );

    public GroupAnalyticsInteractor(GroupAnalyticsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(GroupAnalyticsInputData inputData) {
        try {
            List<UserTasteProfile> profiles = inputData.getProfiles();

            if (profiles.isEmpty()) {
                presenter.prepareFailView("Group must have at least one member.");
                return;
            }
            if (profiles.size() > MAX_GROUP_SIZE) {
                presenter.prepareFailView("Group cannot have more than " + MAX_GROUP_SIZE + " members.");
                return;
            }

            List<PairwiseSimilarityData> pairwise = computePairwiseSimilarities(profiles);
            double avgSim = computeAverageSimilarity(pairwise);
            Map<String, Double> vibeScores = computeVibeScores(profiles);
            GroupVerdict verdict = chooseVerdict(vibeScores, avgSim);

            GroupAnalyticsOutputData out = new GroupAnalyticsOutputData(pairwise, avgSim, vibeScores, verdict);
            presenter.prepareSuccessView(out);

        } catch (Exception e) {
            presenter.prepareFailView("Unexpected error: " + e.getMessage());
        }
    }

    private List<PairwiseSimilarityData> computePairwiseSimilarities(List<UserTasteProfile> profiles) {
        List<PairwiseSimilarityData> results = new ArrayList<>();
        for (int i = 0; i < profiles.size(); i++) {
            for (int j = i + 1; j < profiles.size(); j++) {
                UserTasteProfile a = profiles.get(i);
                UserTasteProfile b = profiles.get(j);
                double sim = jaccard(a.getGenres(), b.getGenres());
                results.add(new PairwiseSimilarityData(a.getUsername(), b.getUsername(), sim));
            }
        }
        return results;
    }

    private double jaccard(Set<String> a, Set<String> b) {
        if (a.isEmpty() && b.isEmpty()) return 1.0;
        Set<String> inter = new HashSet<>(a);
        inter.retainAll(b);
        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        return union.isEmpty() ? 0.0 : (double) inter.size() / union.size();
    }

    private double computeAverageSimilarity(List<PairwiseSimilarityData> pairs) {
        if (pairs.isEmpty()) return 1.0;
        double sum = 0.0;
        for (PairwiseSimilarityData p : pairs) sum += p.getSimilarity();
        return sum / pairs.size();
    }

    private Map<String, Double> computeVibeScores(List<UserTasteProfile> profiles) {
        int dance = 0, cry = 0, road = 0, old = 0, pod = 0;
        int total = 0;

        for (UserTasteProfile p : profiles) {
            for (String g : p.getGenres()) {
                total++;
                if (matchesAny(g, DANCE_GENRES)) dance++;
                if (matchesAny(g, CRY_GENRES)) cry++;
                if (matchesAny(g, ROADTRIP_GENRES)) road++;
                if (matchesAny(g, OLD_SOUL_GENRES)) old++;
                if (matchesAny(g, PODCASTY_GENRES)) pod++;
            }
        }

        Map<String, Double> map = new LinkedHashMap<>();
        if (total == 0) {
            map.put("DanceTogether", 0.0);
            map.put("CryTogether", 0.0);
            map.put("RoadTrip", 0.0);
            map.put("OldSoul", 0.0);
            map.put("StartPodcast", 0.0);
            return map;
        }

        map.put("DanceTogether", (double) dance / total);
        map.put("CryTogether", (double) cry / total);
        map.put("RoadTrip", (double) road / total);
        map.put("OldSoul", (double) old / total);
        map.put("StartPodcast", (double) pod / total);
        return map;
    }

    private boolean matchesAny(String genre, Set<String> keywords) {
        for (String key : keywords) {
            if (genre.contains(key)) return true;
        }
        return false;
    }

    private GroupVerdict chooseVerdict(Map<String, Double> vibes, double avgSim) {
        if (avgSim < 0.25) return GroupVerdict.CHAOTIC_MISMATCH;

        String bestKey = null;
        double bestScore = -1;
        for (var e : vibes.entrySet()) {
            if (e.getValue() > bestScore) {
                bestScore = e.getValue();
                bestKey = e.getKey();
            }
        }

        if (bestKey == null) return GroupVerdict.CHAOTIC_MISMATCH;

        return switch (bestKey) {
            case "DanceTogether" -> GroupVerdict.DANCE_TOGETHER;
            case "CryTogether" -> GroupVerdict.CRY_TOGETHER;
            case "RoadTrip" -> GroupVerdict.ROAD_TRIP_BUDDIES;
            case "OldSoul" -> GroupVerdict.OLD_SOUL_BOOK_CLUB;
            case "StartPodcast" -> GroupVerdict.START_A_PODCAST;
            default -> GroupVerdict.CHAOTIC_MISMATCH;
        };
    }
}
