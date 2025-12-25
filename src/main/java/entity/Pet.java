package entity;

public class Pet {
    private String id;
    private String name;
    private String type;
    private String breed;
    private String age;
    private String gender;
    private String size;
    private String contact;

    public Pet(String id, String name, String type, String breed,
               String age, String gender, String size, String contact) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.size = size;
        this.contact = contact;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String  getType() { return type; }
    public String getBreed() { return breed; }
    public String getAge() { return age; }
    public String getGender() { return gender; }
    public String getSize() { return size; }
    public String getContact() { return contact; }


    @Override
    public String toString() {
        return name + " (" + type + ", " + breed + ", " + gender + ")";
    }
}

