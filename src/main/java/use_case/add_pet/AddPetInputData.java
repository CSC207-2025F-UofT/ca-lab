package use_case.add_pet;

public class AddPetInputData {
    private String name;
    private String type;
    private String breed;
    private String age;
    private String gender;
    private String size;
    private String contact;

    public AddPetInputData(String name, String type, String breed, String age, String gender, String size,
                           String contact) {

        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.size = size;
        this.contact = contact;
    }

    public String getName() {return name;}
    public String getType() {return type;}
    public String getBreed() {return breed;}
    public String getAge() {return age;}
    public String getGender() {return gender;}
    public String getSize() {return size;}
    public String getContact() {return contact;}

}
