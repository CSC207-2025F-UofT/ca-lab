package use_case.browse_filter;

import data_access.PetAPIGatewayInterface;
import entity.Pet;
import java.util.List;

public class BrowseFilterInteractor implements BrowseFilterInputBoundary {
    private final PetAPIGatewayInterface petGateway;
    private final BrowseFilterOutputBoundary presenter;

    public BrowseFilterInteractor(PetAPIGatewayInterface petGateway, BrowseFilterOutputBoundary presenter) {
        this.petGateway = petGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(BrowseFilterInputData inputData) {
        List<Pet> pets = petGateway.fetchPets(inputData.getType(), inputData.getGender());
        BrowseFilterOutputData outputData = new BrowseFilterOutputData(pets);
        presenter.present(outputData);
    }
}
