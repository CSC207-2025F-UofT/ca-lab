package use_case.delete_pet;

public interface DeletePetOutputBoundary {
    void preparePetView(DeletePetOutputData petData);

    void prepareWarningView(String warningMessage);

    void prepareSuccessView(DeletePetOutputData outputData);

    void prepareFailureView(String errorMessage);
}
