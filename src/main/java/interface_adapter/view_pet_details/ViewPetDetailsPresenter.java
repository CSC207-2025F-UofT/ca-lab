package interface_adapter.view_pet_details;

import entity.Pet;
import interface_adapter.ViewManagerModel;
import interface_adapter.submit_application.SubmitViewModel;
import use_case.view_pet_details.ViewPetDetailsOutputBoundary;
import use_case.view_pet_details.ViewPetDetailsOutputData;

public class ViewPetDetailsPresenter implements ViewPetDetailsOutputBoundary {

    private final ViewPetDetailsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final SubmitViewModel submitViewModel;

    public ViewPetDetailsPresenter(ViewPetDetailsViewModel viewModel,
                                   ViewManagerModel viewManagerModel,
                                   SubmitViewModel submitViewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.submitViewModel = submitViewModel;
    }

    @Override
    public void present(ViewPetDetailsOutputData outputData) {
        Pet pet = outputData.getPet();
        ViewPetDetailsState state = new ViewPetDetailsState();

        if (pet == null) {
            state.setErrorMessage("Pet details not found.");
            return;
        } else {
            state.setPetId(pet.getId());
            state.setPetName(pet.getName());
            state.setPetType(pet.getType());
            state.setPetBreed(pet.getBreed());
            state.setPetAge(pet.getAge());
            state.setPetGender(pet.getGender());
            state.setPetSize(pet.getSize());
            state.setPetContact(pet.getContact());
        }
        viewModel.setState(state);
    }

    @Override
    public void switchToApplicationView() {
        // Switch to the Submit Application screen
        viewManagerModel.setState(submitViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
