package use_case.submit_application;

public class SubmitInputData {
    private final String petId;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String age;
    private final String occupation;
    private final String address;
    private final String homeEvi;
    private final String tel;
    private final String email;
    private final String reason;
    private final String prevExp;
    private final String availability;

    public SubmitInputData(String petId, String username, String firstname, String lastname, String age,
                           String occupation, String address, String homeEvi, String tel, String email, String reason,
                           String prevExp, String availability) {
        this.petId = petId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.occupation = occupation;
        this.address = address;
        this.homeEvi = homeEvi;
        this.tel = tel;
        this.email = email;
        this.reason = reason;
        this.prevExp = prevExp;
        this.availability = availability;
    }

    public String getPetId() {
        return petId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAge() {
        return age;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getAddress() {
        return address;
    }

    public String getHomeEvi() {
        return homeEvi;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public String getReason() {
        return reason;
    }

    public String getPrevExp() {
        return prevExp;
    }

    public String getAvailability() {
        return availability;
    }

}
