package use_case.submit_application;

public interface SubmitInputBoundary {

    void execute(SubmitInputData submitInputData);

    void switchToBrowserFilterView();

}
