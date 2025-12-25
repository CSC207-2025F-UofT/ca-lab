package use_case.delete_pet;

import entity.Pet;

public interface DeletePetDataAccessInterface {
    boolean existsPet(String id);

    void deletePet(String id);

    Pet getPet(String id);
}
