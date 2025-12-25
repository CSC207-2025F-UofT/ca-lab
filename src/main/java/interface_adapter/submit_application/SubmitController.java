package interface_adapter.submit_application;

import use_case.submit_application.SubmitInputBoundary;
import use_case.submit_application.SubmitInputData;

public class SubmitController {
    private final SubmitInputBoundary submitUseCaseInteractor;


    public SubmitController(SubmitInputBoundary submitUseCaseInteractor) {
        this.submitUseCaseInteractor = submitUseCaseInteractor;
    }

    public void execute(String petId, String username, String firstname, String lastname, String age, String occupation,
                        String address, String homeEvi, String tel, String email, String reason,
                        String prevExp, String availability) {
        final SubmitInputData submitInputData = new SubmitInputData(petId, username, firstname, lastname, age, occupation,
                address, homeEvi, tel, email, reason, prevExp, availability);

        submitUseCaseInteractor.execute(submitInputData);

    }

    public void switchToBrowserFilterView() {
        submitUseCaseInteractor.switchToBrowserFilterView();
    }

    public void autoFill() {
        submitUseCaseInteractor.autofill();
    }
}
