package data_access;

import entity.Pet;
import java.util.List;

public interface PetAPIGatewayInterface {
    List<Pet> fetchPets(String species, String gender);
    Pet fetchPetById(String petId);
}
