package interface_adapter.manage_application;

// View Model is the object that contains all the information that View needs to present
public class ApplicationCardViewModel {
    // this class is a single application card listed in the stack of applications shown to admin
    public final String application_id;
    public final String applicant_name;
    public final String pet_id;

    public ApplicationCardViewModel(String application_id, String first_name, String last_name, String pet_id) {

        this.application_id = application_id;
        this.applicant_name = first_name + " " + last_name;
        this.pet_id = pet_id;
    }


}
