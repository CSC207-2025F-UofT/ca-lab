package interface_adapter.view_pet_details;

import entity.Pet;
import use_case.view_pet_details.ViewPetDetailsOutputBoundary;
import use_case.view_pet_details.ViewPetDetailsOutputData;

public class ViewPetDetailsPresenter implements ViewPetDetailsOutputBoundary {

    private final ViewPetDetailsViewModel viewModel;

    public ViewPetDetailsPresenter(ViewPetDetailsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ViewPetDetailsOutputData outputData) {
        Pet pet = outputData.getPet();

        if (pet == null) {
            viewModel.setErrorMessage("Pet details not found.");
            viewModel.firePropertyChange();
            return;
        }

        viewModel.setPetName(pet.getName());
        viewModel.setPetType(pet.getType());
        viewModel.setPetBreed(pet.getBreed());
        viewModel.setPetAge(pet.getAge());
        viewModel.setPetGender(pet.getGender());
        viewModel.setPetSize(pet.getSize());
        viewModel.setPetContact(pet.getContact());

        viewModel.firePropertyChange();
    }
}
