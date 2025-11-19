package interface_adapter.browse_filter;

import entity.Pet;
import use_case.browse_filter.BrowseFilterOutputBoundary;
import use_case.browse_filter.BrowseFilterOutputData;

import java.util.List;
import java.util.stream.Collectors;

public class BrowseFilterPresenter implements BrowseFilterOutputBoundary {

    private final BrowseFilterViewModel viewModel;

    public BrowseFilterPresenter(BrowseFilterViewModel vm) {
        this.viewModel = vm;
    }

    @Override
    public void present(BrowseFilterOutputData outputData) {
        List<String> petStrings = outputData.getPets().stream()
                .map(p -> p.getName() + " | " + p.getType() + " | " + p.getBreed() + " | " + p.getGender())
                .collect(Collectors.toList());

        // ★ Access the state inside the ViewModel
        BrowseFilterState state = viewModel.getState();
        state.setPets(petStrings);

        // ★ Notify the view that P E T S changed
        viewModel.firePropertyChange("pets");
    }
}
