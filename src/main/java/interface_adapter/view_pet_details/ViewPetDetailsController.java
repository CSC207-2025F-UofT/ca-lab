package interface_adapter.view_pet_details;

import use_case.view_pet_details.ViewPetDetailsInputBoundary;
import use_case.view_pet_details.ViewPetDetailsInputData;

public class ViewPetDetailsController {
    private final ViewPetDetailsInputBoundary interactor;

    public ViewPetDetailsController(ViewPetDetailsInputBoundary interactor) { this.interactor = interactor;}

    public void execute(String petID) {
        ViewPetDetailsInputData inputData = new ViewPetDetailsInputData(petID);
        interactor.execute(inputData);
    }

}
