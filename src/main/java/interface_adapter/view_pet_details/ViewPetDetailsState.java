package interface_adapter.view_pet_details;

public class ViewPetDetailsState {
    private String petId;
    private String petName;
    private String petType;
    private String petBreed;
    private String petAge;
    private String petGender;
    private String petSize;
    private String petContact;
    private String errorMessage;

    // --- Setters ---
    public void setPetId(String petId) { this.petId = petId; }
    public void setPetName(String name) { this.petName = name; }
    public void setPetType(String type) { this.petType = type; }
    public void setPetBreed(String breed) { this.petBreed = breed; }
    public void setPetAge(String age) { this.petAge = age; }
    public void setPetGender(String gender) { this.petGender = gender; }
    public void setPetSize(String size) { this.petSize = size; }
    public void setPetContact(String contact) { this.petContact = contact; }
    public void setErrorMessage(String message) { this.errorMessage = message; }

    // --- Getters ---
    public String getPetId() { return petId; }
    public String getPetName() { return petName; }
    public String getPetType() { return petType; }
    public String getPetBreed() { return petBreed; }
    public String getPetAge() { return petAge; }
    public String getPetGender() { return petGender; }
    public String getPetSize() { return petSize; }
    public String getPetContact() { return petContact; }
    public String getErrorMessage() { return errorMessage; }
}
