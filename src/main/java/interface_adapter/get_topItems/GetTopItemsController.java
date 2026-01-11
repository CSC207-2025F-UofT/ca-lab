package interface_adapter.get_topItems;

import entity.SpotifyUser;
import use_case.get_topItems.*;

public class GetTopItemsController {
    private final GetTopItemsInputBoundary GetTopSongSongsUseCaseInteractor;

    public GetTopItemsController(GetTopItemsInputBoundary GetTopSongSongsUseCaseInteractor){
        this.GetTopSongSongsUseCaseInteractor = GetTopSongSongsUseCaseInteractor;
    }

    public void execute(SpotifyUser spotifyUser, TopItem topItem, TimeRange timeRange){
        GetTopItemsInputData inputData = new GetTopItemsInputDataBuilder()
                .setTopItem(topItem)
                .setTimeType(timeRange)
                .setSpotifyUser(spotifyUser)
                .createGetTopSongsInputData();
        GetTopSongSongsUseCaseInteractor.execute(inputData);
    }
}
