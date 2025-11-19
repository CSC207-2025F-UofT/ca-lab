package interface_adapter.signup;

/**
 * The state for the Signup View Model.
 */
public class SignupState {
    private String firstname = "";
    private String firstnameError = "";
    private String lastname = "";
    private String lastnameError = "";
    private String username = "";
    private String usernameError = "";
    private String password = "";
    private String passwordError = "";
    private String repeatPassword = "";
    private String repeatPasswordError = "";

    // --- Getters ---
    public String getFirstname() {
        return firstname;
    }
    public String getFirstnameError() {return firstnameError;}

    public String getLastname() {return lastname;}

    public String getLastnameError() {return lastnameError;}

    public String getUsername() {
        return username;
    }

    public String getUsernameError() {
        return usernameError;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public String getRepeatPasswordError() {
        return repeatPasswordError;
    }

    // --- Setters ---

    public void setFirstname(String firstname) {this.firstname = firstname;}

    public void setFirstnameError(String firstnameError) {this.firstnameError = firstnameError;}

    public void setLastname(String lastname) {this.lastname = lastname;}

    public void setLastnameError(String lastnameError) {this.lastnameError = lastnameError;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void setRepeatPasswordError(String repeatPasswordError) {
        this.repeatPasswordError = repeatPasswordError;
    }

    @Override
    public String toString() {
        return "SignupState{"
                + "firstname='" + firstname + '\''
                + ", lastname='" + lastname + '\''
                + ", username='" + username + '\''
                + ", password='" + password + '\''
                + ", repeatPassword='" + repeatPassword + '\''
                + '}';
    }
}
