package interface_adapter.browse_filter;

import interface_adapter.ViewModel;

public class BrowseFilterViewModel extends ViewModel<BrowseFilterState> {

    public static final String TITLE_LABEL = "Browse Pets";
    public static final String SPECIES_LABEL = "Species";
    public static final String GENDER_LABEL = "Gender";
    public static final String APPLY_FILTERS_BUTTON_LABEL = "Apply Filters";

    public BrowseFilterViewModel() {
        super("browse filter");
        setState(new BrowseFilterState());
    }
}

