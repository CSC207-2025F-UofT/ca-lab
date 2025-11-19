package use_case.browse_filter;

import entity.Pet;
import java.util.List;

public class BrowseFilterOutputData {
    private final List<Pet> pets;

    public BrowseFilterOutputData(List<Pet> pets) {
        this.pets = pets;
    }

    public List<Pet> getPets() {
        return pets;
    }
}
