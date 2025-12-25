package interface_adapter.submit_application;

public class SubmitState {

    private String petId = "";
    private String username = "";
    private String firstname = "";
    private String lastname = "";
    private String address = "";
    private String homeEvi = "";
    private String occupation = "";
    private String tel = "";
    private String email = "";
    private String age = "";
    private String reason = "";
    private String prevExp = "";
    private String availability = "";
    private String error = null;
    private String success = null;


    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
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

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPrevExp() {
        return prevExp;
    }
    public void setPrevExp(String prevExp) {
        this.prevExp = prevExp;
    }

    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
