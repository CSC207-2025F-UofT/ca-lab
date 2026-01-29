package use_case.get_topItems;

public interface GetTopItemsOutputBoundary {
    void prepareSuccessView(GetTopItemsOutputData outputData);
    void prepareFailureView(String errorMessage);
}
