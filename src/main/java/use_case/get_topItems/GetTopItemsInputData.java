package use_case.get_topItems;

import entity.SpotifyUser;

public class GetTopItemsInputData {
    private final TopItem topItem;
    private final TimeRange timeRange;
    private final SpotifyUser spotifyUser;

    public GetTopItemsInputData(TopItem topItem, TimeRange timeRange, SpotifyUser spotifyUser) {
        this.timeRange = timeRange;
        this.topItem = topItem;
        this.spotifyUser = spotifyUser;
    }

    public TopItem getTopItem() {
        return this.topItem;
    }
    public TimeRange getTimeType() {
        return this.timeRange;
    }
    public SpotifyUser getSpotifyUser() {
        return this.spotifyUser;
    }
}
