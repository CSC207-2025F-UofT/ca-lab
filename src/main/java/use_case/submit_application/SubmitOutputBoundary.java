package use_case.submit_application;

public interface SubmitOutputBoundary {

    void prepareSuccessView(String successMessage);

    void prepareFailView(String errorMessage);

    void switchToBrowserFilterView();

}
