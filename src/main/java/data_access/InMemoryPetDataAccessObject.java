package data_access;

import entity.Pet;
import use_case.add_pet.AddPetDataAccessInterface;
import use_case.delete_pet.DeletePetDataAccessInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryPetDataAccessObject implements AddPetDataAccessInterface, DeletePetDataAccessInterface {
    private final Map<String, Pet> pets = new HashMap<>();


    // Optional: expose for debugging/testing
    public Collection<Pet> getAllPets() {
        return pets.values();
    }

    @Override
    public boolean existsPet(List<String> petData) {
        for (Pet pet : pets.values()) {
            if (pet.getName().equals(petData.get(0))) {
                if(pet.getGender().equals(petData.get(1))
                        &&pet.getAge().equals(petData.get(2))
                        &&pet.getType().equals(petData.get(3))
                        &&pet.getSize().equals(petData.get(4))
                        &&pet.getBreed().equals(petData.get(5))
                ){
                    return true;
                }
                return false;
            }
        }
        return false;
    };

    @Override
    public void add(Pet pet) {
        pets.put(pet.getId(), pet);
    }
    @Override
    public String generateId(String type) {
        String prefix = type.toUpperCase().replaceAll("\\s+", "");
        long count = pets.values().stream()
                .filter(p -> p.getType().equalsIgnoreCase(type))
                .count();

        long next = count + 1;

        return prefix + "-" + String.format("%03d", next);
    }

    @Override
    public boolean existsPet(String id) {
        return pets.containsKey(id);
    }

    @Override
    public void deletePet(String id) {
        pets.remove(id);
    }

    @Override
    public Pet getPet(String id) {
        return pets.get(id);
    }

}

