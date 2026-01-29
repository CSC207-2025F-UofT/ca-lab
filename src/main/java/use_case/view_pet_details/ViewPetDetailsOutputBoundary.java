package use_case.view_pet_details;

public interface ViewPetDetailsOutputBoundary {
    void present(ViewPetDetailsOutputData outputData);

    void switchToApplicationView();
}
