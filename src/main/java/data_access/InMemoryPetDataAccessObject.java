package data_access;

import entity.Pet;
import use_case.add_pet.AddPetDataAccessInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryPetDataAccessObject implements AddPetDataAccessInterface {
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
}

