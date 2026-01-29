package interface_adapter.delete_pet;

import use_case.delete_pet.*;

public class DeletePetPresenter implements DeletePetOutputBoundary {

    private final DeletePetViewModel viewModel;

    public DeletePetPresenter(DeletePetViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void preparePetView(DeletePetOutputData petData) {
        viewModel.setPetPreviewMessage(petData.getMessage());
    }

    @Override
    public void prepareWarningView(String warningMessage) {
        viewModel.setWarningMessage(warningMessage);
    }

    @Override
    public void prepareSuccessView(DeletePetOutputData outputData) {
        viewModel.setStatusMessage(outputData.getMessage());
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        viewModel.setStatusMessage("Error: " + errorMessage);
    }
}

