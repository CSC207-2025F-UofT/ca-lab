package use_case.add_pet;

public class AddPetOutputData {
    private final String id;
    private final String message;
    private final boolean success;

    public AddPetOutputData(String id, String message, boolean success) {
        this.id = id;
        this.message = message;
        this.success = success;
    }

    public String getId() {return id;}

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}


