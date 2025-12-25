package interface_adapter.add_pet;

import use_case.add_pet.AddPetOutputBoundary;
import use_case.add_pet.AddPetOutputData;

public class AddPetPresenter implements AddPetOutputBoundary {

    private final AddPetViewModel viewModel;

    public AddPetPresenter(AddPetViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(AddPetOutputData data) {
        viewModel.setStatusMessage("Pet added! ID = " + data.getId());
        //viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setStatusMessage("Failed to add pet: " + errorMessage);
        //viewModel.firePropertyChanged();
    }
}

