package use_case.signup;

/**
 * The Input Data for the Signup Use Case.
 */
public class SignupInputData {

    private final String userType;
    private final String firstname;
    private final String lastname;
    private final String username;
    private final String password;
    private final String repeatPassword;

    public SignupInputData(String userType, String firstname, String lastname,
                           String username, String password, String repeatPassword) {
        this.userType = userType;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public String getUserType() {return userType;}

    public String getFirstname() {return firstname;}

    public String getLastname() {return lastname;}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }
}
