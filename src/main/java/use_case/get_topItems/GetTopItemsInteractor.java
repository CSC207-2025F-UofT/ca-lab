package use_case.get_topItems;

public class GetTopItemsInteractor implements GetTopItemsInputBoundary {
    private final GetTopItemsOutputBoundary presenter;
    private final GetTopItemsUserDataAccessInterface dataAccess;

    public GetTopItemsInteractor(GetTopItemsOutputBoundary presenter,
                                 GetTopItemsUserDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(GetTopItemsInputData input) {
        try{
            GetTopItemsOutputData outputData = dataAccess.fetchSpotifyResult(input);
            presenter.prepareSuccessView(outputData);
        } catch (Exception e){
            presenter.prepareFailureView("Failed to fetch spotify result"
                    + e.getMessage());
        }
    }
}
