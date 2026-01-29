package use_case.add_pet;

import entity.Pet;

import java.util.List;

public interface AddPetDataAccessInterface {
    /**
     * Returns true if a pet with this name already exists.
     */
    boolean existsPet(List<String> petData);

    /**
     * Saves a new pet to the data store.
     */
    void add(Pet pet);

    String generateId(String type);

    boolean existsPet(String id);
}
