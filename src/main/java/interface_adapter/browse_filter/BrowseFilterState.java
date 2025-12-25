package interface_adapter.browse_filter;

import java.util.ArrayList;
import java.util.List;

/**
 * The state for the Browse/Filter View Model.
 */
public class BrowseFilterState {

    private String selectedSpecies = "";
    private String selectedGender = "";
    private String speciesError = "";
    private String genderError = "";

    // The list of pets the UI should display
    private List<String> pets = new ArrayList<>();

    // --- Getters ---
    public String getSelectedSpecies() {
        return selectedSpecies;
    }

    public String getSelectedGender() {
        return selectedGender;
    }

    public String getSpeciesError() {
        return speciesError;
    }

    public String getGenderError() {
        return genderError;
    }

    public List<String> getPets() {
        return pets;
    }

    // --- Setters ---
    public void setSelectedSpecies(String selectedSpecies) {
        this.selectedSpecies = selectedSpecies;
    }

    public void setSelectedGender(String selectedGender) {
        this.selectedGender = selectedGender;
    }

    public void setSpeciesError(String speciesError) {
        this.speciesError = speciesError;
    }

    public void setGenderError(String genderError) {
        this.genderError = genderError;
    }

    public void setPets(List<String> pets) {
        this.pets = pets;
    }
}
