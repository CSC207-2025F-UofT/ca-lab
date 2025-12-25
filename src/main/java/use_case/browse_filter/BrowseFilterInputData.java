package use_case.browse_filter;

public class BrowseFilterInputData {
    private final String type;
    private final String gender;

    public BrowseFilterInputData(String type, String gender) {
        this.type = type;
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public String getGender() {
        return gender;
    }
}
