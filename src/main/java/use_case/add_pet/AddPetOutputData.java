package use_case.add_pet;

public class AddPetOutputData {
    private final String message;
    private final boolean success;

    public AddPetOutputData(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}


