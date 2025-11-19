package entity;

public class Visitor extends User{

    private String firstName;
    private String lastName;
    private String address;
    private String homeEvi;
    private String occupation;
    private String phoneNum;
    private String email;
    private String age;
    private String phoneNumber;
    private String email;

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     *
     * @param name     the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public Visitor(String name, String password) {
        super(name, password);
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.homeEvi = "";
        this.occupation = "";
        this.email = "";
        this.phoneNum = "";
        this.age = "";
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomeEvi() {
        return homeEvi;
    }

    public void setHomeEvi(String homeEvi) {
        this.homeEvi = homeEvi;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

}
