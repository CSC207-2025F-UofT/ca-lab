package interface_adapter.add_pet;

import use_case.add_pet.AddPetInputData;
import use_case.add_pet.AddPetInputBoundary;

public class AddPetController {

    private final AddPetInputBoundary addPetUsecaseInteractor;

    public AddPetController(AddPetInputBoundary addPetUsecaseInteractor) {
        this.addPetUsecaseInteractor = addPetUsecaseInteractor;
    }

    public void execute(
                        String name,
                        String type,
                        String breed,
                        String age,
                        String gender,
                        String size,
                        String contact) {

        AddPetInputData inputData = new AddPetInputData(name, type, breed, age, gender, size, contact);

        addPetUsecaseInteractor.execute(inputData);
    }
}

