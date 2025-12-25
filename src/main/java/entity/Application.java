package entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Application {

    // Class variable to keep track of how many applcations, used as id

    // Status of the application, for application processing
    private String status;
    // NOTE: status has to be strictly in the following set:
    // {processing, approved, rejected}

    // Application ID for internal use
    private String application_id;

    // Applicant Information
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private String age;
    private String occupation;

    // Which Pet to adopt by id
    private String pet_id;

    //
    private String reason_to_adopt;
    private String home_environment; // describe, apartment, small, big, etc.
    // give hint on how to fill out each field
    private String availability;
    // explain their availability
    private String previous_experience;

    /**
     * Creates a new application for database->program display interaction
     */
    public Application(String application_id, String pet_id, String first_name, String last_name, String email,
                       String phone_number, String age, String occupation, String reason, String environment,
                       String availability, String experience) {
        // 12 parameters for database -> program constructor
        this.application_id = application_id;
        this.pet_id = pet_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.age = age;
        this.occupation = occupation;
        this.reason_to_adopt = reason;
        this.home_environment = environment;
        this.availability = availability;
        this.previous_experience = experience;
        this.status = "processing";

    }

    /**
     * Creates a new application for user->program interaction
     * @throws IllegalArgumentException if the password or name are empty
     */
    //
    public Application(String pet_id, String first_name, String last_name, String email,
                       String phone_number, String age, String occupation, String reason_to_adopt,
                       String home_environment, String availability, String previous_experience) {
        // 11 parameters for user -> program constructor


        // set all attributes
        this.pet_id = pet_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.age = age;
        this.occupation = occupation;
        this.reason_to_adopt = reason_to_adopt;
        this.home_environment = home_environment;
        this.availability = availability;
        this.previous_experience = previous_experience;
        this.application_id = UUID.randomUUID().toString();; // use counter count as id of application
        this.status = "processing";

    }

    // Getter methods
    public String getFirstName() {
        return first_name;
    }
    public String getLastName() { return last_name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phone_number; }
    public String getAge() { return age; }
    public String getOccupation() { return occupation; }
    public String getPetId() {return pet_id;}
    public String getReason() {return reason_to_adopt;}
    public String getHomeEnvironment() {return home_environment;}
    public String getPreviousExperience() {return previous_experience;}
    public String getAvailability() {return availability;}

    public String getApplicationId() {return application_id;}
    public String getStatus() {return status;}

    // Need to be able to set Status after the admin reviewed the application
    public String setStatus(String status) {return this.status = status;}

}

// NOTE: in submit_application, raise an error if any field isn't filled out
