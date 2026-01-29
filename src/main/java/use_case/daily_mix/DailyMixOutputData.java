package use_case.daily_mix;

import java.util.List;

public class DailyMixOutputData {
    private final List<String> tracks;  // 每一行已经是 "歌名 - 艺人"

    public DailyMixOutputData(List<String> tracks) {
        this.tracks = tracks;
    }

    public List<String> getTracks() {
        return tracks;
    }
}

