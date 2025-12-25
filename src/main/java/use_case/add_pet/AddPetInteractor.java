package use_case.add_pet;

import entity.Pet;
import entity.PetFactory;

import java.util.ArrayList;
import java.util.List;

public class AddPetInteractor implements AddPetInputBoundary {

    private final PetFactory petFactory;
    private final AddPetDataAccessInterface petRepo;
    private final AddPetOutputBoundary presenter;


    public AddPetInteractor(PetFactory petFactory,
                            AddPetDataAccessInterface petRepo,
                            AddPetOutputBoundary presenter) {
        this.petFactory = petFactory;
        this.petRepo = petRepo;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddPetInputData inputData) {

        // build the Pet attributes
        if (inputData.getName().isEmpty() ||
                inputData.getType().isEmpty() ||
                inputData.getBreed().isEmpty() ||
                inputData.getAge().isEmpty() ||
                inputData.getGender().isEmpty() ||
                inputData.getSize().isEmpty() ||
                inputData.getContact().isEmpty()) {

            presenter.prepareFailView("All fields must be filled out.");
            return;
        }

        // Generate a single ID from the repo
        String petId = petRepo.generateId(inputData.getType());

        List<String> attributes = List.of(
                inputData.getName(),
                inputData.getGender(),
                inputData.getAge(),
                inputData.getType(),
                inputData.getSize(),
                inputData.getBreed()
        );

        if (petRepo.existsPet(attributes)) {
            presenter.prepareFailView("A pet already exists.");
            return;
        }

        // Create Pet object
        Pet pet = petFactory.create(
                petId,
                inputData.getName(),
                inputData.getType(),
                inputData.getBreed(),
                inputData.getAge(),
                inputData.getGender(),
                inputData.getSize(),
                inputData.getContact()
        );

        // Save
        petRepo.add(pet);

        // Output
        AddPetOutputData outputData =
                new AddPetOutputData(petId, "Pet added successfully!", true);

        presenter.prepareSuccessView(outputData);
    }
}




