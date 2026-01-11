package use_case.group_analytics;

import entity.UserTasteProfile;

import java.util.Collections;
import java.util.List;

public class GroupAnalyticsInputData {

    private final List<UserTasteProfile> profiles;

    public GroupAnalyticsInputData(List<UserTasteProfile> profiles) {
        this.profiles = Collections.unmodifiableList(profiles);
    }

    public List<UserTasteProfile> getProfiles() {
        return profiles;
    }
}
