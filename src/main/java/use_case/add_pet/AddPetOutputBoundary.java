package use_case.add_pet;

public interface AddPetOutputBoundary {
    void prepareSuccessView(AddPetOutputData outputData);

    void prepareFailView(String errorMessage);

}
