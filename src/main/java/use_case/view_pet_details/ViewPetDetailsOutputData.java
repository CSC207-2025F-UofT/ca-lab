package use_case.view_pet_details;

import entity.Pet;

public class ViewPetDetailsOutputData {
    private final Pet pet;

    public ViewPetDetailsOutputData(Pet pet) {
        this.pet = pet;
    }

    public Pet getPet() {
        return pet;
    }
}
