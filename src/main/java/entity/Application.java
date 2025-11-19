
package entity;
import java.util.ArrayList;
import java.util.List;

public class Application {

    // Class variable to keep track of how many applcations, used as id
    public static int counter = 0;

    // Application ID for internal use
    private String application_id;

    // Applicant Information
    private Visitor user;

    // Which Pet to adopt by id
    private String pet_id;

    //
    private String reason_to_adopt;
    private String home_environment; // describe, apartment, small, big, etc.
    // give hint on how to fill out each field
    private String availability;
    // explain their availability
    private String previous_experience;
    private List<String> personal_information;

    // helper method, to gather all info from the user
    private List<String> getPersonalInformation(Visitor user) {
        // get all personal info from visitor
        String first_name = user.getFirstName();
        String last_name = user.getLastName();
        String email = user.getEmail();
        String phone_number = user.getPhoneNum();
        String age = user.getAge();
        String occupation = user.getOccupation();

        List<String> personal_information = new ArrayList<>();
        personal_information.add(first_name);
        personal_information.add(last_name);
        personal_information.add(email);
        personal_information.add(phone_number);
        personal_information.add(age);
        personal_information.add(occupation);

        return personal_information;
    }

    /**
     * Creates a new application
     * @throws IllegalArgumentException if the password or name are empty
     */
    //
    public Application(Visitor user, String pet_id, String reason_to_adopt, String home_environment, String availability, String previous_experience) {
        List<String> personal_information = getPersonalInformation(user);
        // loop through the list, see if any is empty, if yes throw an error
        for (String info : personal_information) {
            if ("".equals(info)) {
                throw new IllegalArgumentException("This information cannot be empty." +
                        "Please fill out your personal information in setting.");
            }
        }
        // set all attributes
        this.pet_id = pet_id;
        this.personal_information = personal_information;
        this.reason_to_adopt = reason_to_adopt;
        this.home_environment = home_environment;
        this.availability = availability;
        this.previous_experience = previous_experience;
        this.application_id = Integer.toString(counter); // use counter count as id of application
        counter++; // increase counter count by 1

    }

    // Getter methods
    public List<String> getPersonalInformation() {
        return personal_information;
    }
    public String getReason() {
        return reason_to_adopt;
    }
    public String getHomeEnvironment() {return home_environment;}
    public String getPreviousExperience() {return previous_experience;}
    public String getPetId() {return pet_id;}
    public String getApplicationId() {return application_id;}
    public String getAvailability() {return availability;}
    public Visitor getUser() {return user;}

}
