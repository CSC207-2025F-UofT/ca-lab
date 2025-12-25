package interface_adapter.delete_pet;

import use_case.delete_pet.DeletePetInputBoundary;
import use_case.delete_pet.DeletePetInputData;

public class DeletePetController {
    private final DeletePetInputBoundary deletePetUseCaseInteractor;

    public DeletePetController(DeletePetInputBoundary deletePetUseCaseInteractor) {
        this.deletePetUseCaseInteractor = deletePetUseCaseInteractor;
    }

    public void execute(String petId) {
        DeletePetInputData deletePetInputData = new DeletePetInputData(petId);

        deletePetUseCaseInteractor.execute( deletePetInputData );
    }
}
