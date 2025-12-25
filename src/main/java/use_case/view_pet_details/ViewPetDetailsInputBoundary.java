package use_case.view_pet_details;

public interface ViewPetDetailsInputBoundary {
    void execute(ViewPetDetailsInputData inputData);

    void switchToApplicationView();
}
