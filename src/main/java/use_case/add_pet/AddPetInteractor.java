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

        String generatedId = petRepo.generateId(inputData.getType());;
        List<String> petData = new ArrayList<>();

        petData.add(inputData.getName());
        petData.add(inputData.getGender());
        petData.add(inputData.getAge());
        petData.add(inputData.getType());
        petData.add(inputData.getSize());
        petData.add(inputData.getBreed());

        // -------- CHECK EMPTY FIELDS ----------
        if (    inputData.getName().isEmpty() ||
                inputData.getType().isEmpty() ||
                inputData.getBreed().isEmpty() ||
                inputData.getAge().isEmpty() ||
                inputData.getGender().isEmpty() ||
                inputData.getSize().isEmpty() ||
                inputData.getContact().isEmpty()) {

            presenter.prepareFailView("All fields must be filled out.");
            return;
        }

        // -------- CHECK DUPLICATE PET ID ----------
        if (petRepo.existsPet(petData)){
            presenter.prepareFailView("A pet already exists.");
            return;
        }

        // -------- CREATE PET --------
        Pet pet = petFactory.create(
                generatedId,
                inputData.getName(),
                inputData.getType(),
                inputData.getBreed(),
                inputData.getAge(),
                inputData.getGender(),
                inputData.getSize(),
                inputData.getContact()
        );

        petRepo.add(pet);
        //might change to save later

        presenter.prepareSuccessView(new AddPetOutputData("Pet added successfully!", true));
    }
}




