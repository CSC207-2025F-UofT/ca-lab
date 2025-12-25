package use_case.delete_pet;

public class DeletePetOutputData {
    private final String id;
    private final String name;
    private final String type;
    private String message;
    private boolean success;

    public DeletePetOutputData(String id, String name, String type,
                               String message, boolean success) {
        this.message = message;
        this.success = success;
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public String getType() {return type;}
    public String getMessage() {return message;}
    public boolean isSuccess() {return success;}
}
