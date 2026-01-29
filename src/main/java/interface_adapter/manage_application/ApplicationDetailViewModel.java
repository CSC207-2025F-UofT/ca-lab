package interface_adapter.manage_application;

public class ApplicationDetailViewModel {
    public final String applicationId;
    public final String firstName;
    public final String lastName;
    public final String petId;

    public final String email;
    public final String phone;
    public final String occupation;
    public final String age;
    public final String availability;
    public final String reason;
    public final String environment;
    public final String prevExperience;

    public ApplicationDetailViewModel(
            String applicationId, String firstName, String lastName, String petId,
            String email, String phone, String occupation, String age,
            String availability, String reason, String environment,
            String prevExperience
    ) {
        this.applicationId = applicationId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.petId = petId;

        this.email = email;
        this.phone = phone;
        this.occupation = occupation;
        this.age = age;
        this.availability = availability;
        this.reason = reason;
        this.environment = environment;
        this.prevExperience = prevExperience;
    }
}
