package use_case.view_pet_details;

public class ViewPetDetailsInputData {
    private final String petId;

    public ViewPetDetailsInputData(String petId) {
        this.petId = petId;
    }

    public String getPetId() {
        return petId;
    }
}
