package use_case.delete_pet;

import entity.Pet;

public class DeletePetInteractor implements DeletePetInputBoundary{
    private final DeletePetDataAccessInterface petGallery;
    private final DeletePetOutputBoundary presenter;

    public DeletePetInteractor( DeletePetDataAccessInterface petGallery,
                               DeletePetOutputBoundary presenter) {
        this.petGallery = petGallery;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeletePetInputData deletePetInputData) {

        String petId = deletePetInputData.getId();

        if (petGallery.existsPet(petId)) {

            Pet pet = petGallery.getPet(petId);

            DeletePetOutputData petData = new DeletePetOutputData(
                    pet.getId(),
                    pet.getName(),
                    pet.getType(),
                    "Pet ID: " + pet.getId() + "\n" +
                            "Name: " + pet.getName() + "\n" +
                            "Type: " + pet.getType(),
                    false
            );

            presenter.preparePetView(petData);
            presenter.prepareWarningView("Are you sure you want to Delete this Pet?");

            petGallery.deletePet(petId);

            presenter.prepareSuccessView(
                    new DeletePetOutputData(pet.getId(), pet.getName(), pet.getType(),
                            "Pet deleted successfully!", true)
            );
        }
        else {
            presenter.prepareFailureView("Pet Does not Exist.");
        }
    }

}
