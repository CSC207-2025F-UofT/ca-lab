package use_case.get_topItems;

import entity.SpotifyUser;

public class GetTopItemsInputDataBuilder {
    private TopItem topItem;
    private TimeRange timeRange;
    private SpotifyUser spotifyUser;

    public GetTopItemsInputDataBuilder setTopItem(TopItem topItem) {
        this.topItem = topItem;
        return this;
    }

    public GetTopItemsInputDataBuilder setTimeType(TimeRange timeRange) {
        this.timeRange = timeRange;
        return this;
    }

    public GetTopItemsInputDataBuilder setSpotifyUser(SpotifyUser spotifyUser) {
        this.spotifyUser = spotifyUser;
        return this;
    }

    public GetTopItemsInputData createGetTopSongsInputData() {
        return new GetTopItemsInputData(topItem, timeRange, spotifyUser);
    }
}