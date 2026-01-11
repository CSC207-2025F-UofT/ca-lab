package use_case.get_topItems;

public interface GetTopItemsUserDataAccessInterface {
    GetTopItemsOutputData fetchSpotifyResult(GetTopItemsInputData inputData) throws Exception;
}
