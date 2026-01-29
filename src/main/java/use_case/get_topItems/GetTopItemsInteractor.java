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
        System.out.println("=== INTERACTOR: Starting execution ===");
        System.out.println("INTERACTOR: TopItem = " + input.getTopItem());
        System.out.println("INTERACTOR: TimeRange = " + input.getTimeType());

        try {
            GetTopItemsOutputData outputData = dataAccess.fetchSpotifyResult(input);
            System.out.println("INTERACTOR: Got output data with " + outputData.getTopItems().size() + " items");
            presenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            System.err.println("INTERACTOR: Exception caught - " + e.getMessage());
            e.printStackTrace();
            presenter.prepareFailureView("Failed to fetch spotify result: " + e.getMessage());
        }
    }
}
