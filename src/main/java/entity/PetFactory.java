package entity;

import java.util.UUID;

//Factory for creating Pet Objects
public class PetFactory {
    public Pet create(String id, String name, String type, String breed, String age, String gender,
                                String size, String contact) {
        return new Pet(id, name, type,breed, age ,gender,size,contact);
        }

    public String generateId(String type) {
        String prefix = type.toUpperCase().replaceAll("\\s+", "");

        // For now: simple UUID-based ID
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

}
