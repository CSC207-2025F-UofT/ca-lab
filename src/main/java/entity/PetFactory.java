package entity;
//Factory for creating Pet Objects
public class PetFactory {
    public Pet create(String id, String name, String type, String breed, String age, String gender,
                                String size, String contact) {
        return new Pet(id, name, type,breed, age ,gender,size,contact);
        }
}
